#!/usr/bin/env python3
"""
Generates the SVG diagrams used in docs/PHYSICS_TUNING.md.

The curves are computed by a faithful Python port of the library's own
implementation (SplineUtils.computeSplineInfo, AndroidFlingSpline,
FlingCalculator), so every diagram matches what the code actually does.

Usage:
    python3 docs/tools/generate_diagrams.py

Outputs SVG files into docs/images/. Pure standard library, no dependencies.
"""

import math
import os

OUT_DIR = os.path.join(os.path.dirname(__file__), "..", "images")

# ---------------------------------------------------------------------------
# Library defaults (mirrors FlingConfiguration.Builder defaults)
# ---------------------------------------------------------------------------
DEFAULTS = dict(
    scrollViewFriction=0.008,
    decelerationFriction=0.09,
    gravitationalForce=9.80665,
    inchesPerMeter=39.37,
    decelerationRate=math.log(0.78) / math.log(0.9),
    splineInflection=0.1,
    splineStartTension=0.1,
    splineEndTension=1.0,
    numberOfSplinePoints=100,
)

# Reference device used for "real world" plots: Pixel-ish 420 dpi (density 2.625)
DENSITY = 2.625
VELOCITY = 8000.0  # px/s, a typical medium fling

PALETTE = {
    "blue": "#0969da",
    "red": "#cf222e",
    "green": "#1a7f37",
    "purple": "#8250df",
    "orange": "#bc4c00",
    "gray": "#57606a",
    "axis": "#57606a",
    "grid": "#d8dee4",
    "text": "#24292f",
}

FONT = "Helvetica,Arial,sans-serif"


# ---------------------------------------------------------------------------
# Physics (port of AndroidFlingSpline / SplineUtils / FlingCalculator)
# ---------------------------------------------------------------------------
def compute_spline(inflection, start_tension, end_tension, n):
    """Port of SplineUtils.computeSplineInfo. Returns (positions, times)."""
    p1 = start_tension * inflection
    p2 = 1.0 - end_tension * (1.0 - inflection)
    positions = [0.0] * (n + 1)
    times = [0.0] * (n + 1)
    x_min = y_min = 0.0
    for i in range(n):
        alpha = i / n
        x_max = 1.0
        coef = 0.0
        x = 0.0
        while True:
            x = x_min + (x_max - x_min) / 2.0
            coef = 3.0 * x * (1.0 - x)
            tx = coef * ((1.0 - x) * p1 + x * p2) + x ** 3
            if abs(tx - alpha) < 1e-5:
                break
            if tx > alpha:
                x_max = x
            else:
                x_min = x
        positions[i] = coef * ((1.0 - x) * start_tension + x) + x ** 3

        y_max = 1.0
        y = 0.0
        while True:
            y = y_min + (y_max - y_min) / 2.0
            coef = 3.0 * y * (1.0 - y)
            dy = coef * ((1.0 - y) * start_tension + y) + y ** 3
            if abs(dy - alpha) < 1e-5:
                break
            if dy > alpha:
                y_max = y
            else:
                y_min = y
        times[i] = coef * ((1.0 - y) * p1 + y * p2) + y ** 3
    times[n] = 1.0
    positions[n] = 1.0
    return positions, times


class Spline:
    """Port of AndroidFlingSpline: sampled lookup table + linear interpolation."""

    def __init__(self, cfg, n=None):
        n = n or cfg["numberOfSplinePoints"]
        self.n = n
        self.positions, self.times = compute_spline(
            cfg["splineInflection"],
            cfg["splineStartTension"],
            cfg["splineEndTension"],
            n,
        )

    def distance_coefficient(self, t):
        """Port of flingDistanceCoefficient for t in [0, 1]."""
        if t >= 1.0:
            return 1.0
        if t <= 0.0:
            return 0.0
        idx = int(self.n * t)
        if idx >= self.n:
            return 1.0
        t_inf = idx / self.n
        t_sup = (idx + 1) / self.n
        d_inf = self.positions[idx]
        d_sup = self.positions[idx + 1]
        return d_inf + (t - t_inf) * (d_sup - d_inf) / (t_sup - t_inf)

    def velocity_coefficient(self, t):
        """Port of flingVelocityCoefficient for t in [0, 1]."""
        if t >= 1.0:
            return 0.0
        idx = int(self.n * t)
        if idx >= self.n:
            return 0.0
        t_inf = idx / self.n
        t_sup = (idx + 1) / self.n
        d_inf = self.positions[idx]
        d_sup = self.positions[idx + 1]
        return (d_sup - d_inf) / (t_sup - t_inf)


def magic_coefficient(cfg):
    """Port of FlingCalculator.computeDeceleration."""
    return (cfg["gravitationalForce"] * cfg["inchesPerMeter"] * DENSITY * 160.0
            * cfg["decelerationFriction"])


def fling_duration(cfg, velocity=VELOCITY):
    """Fling duration in ms (port of FlingCalculator.flingDuration)."""
    magic = magic_coefficient(cfg)
    l = math.log(cfg["splineInflection"] * abs(velocity)
                 / (cfg["scrollViewFriction"] * magic))
    return 1000.0 * math.exp(l / (cfg["decelerationRate"] - 1.0))


def fling_distance(cfg, velocity=VELOCITY):
    """Fling distance in px (port of FlingCalculator.flingDistance)."""
    magic = magic_coefficient(cfg)
    l = math.log(cfg["splineInflection"] * abs(velocity)
                 / (cfg["scrollViewFriction"] * magic))
    return cfg["scrollViewFriction"] * magic * math.exp(
        cfg["decelerationRate"] / (cfg["decelerationRate"] - 1.0) * l)


def position_trace(cfg, velocity=VELOCITY, steps=400):
    """(times_ms, positions_px) for one fling."""
    duration = fling_duration(cfg, velocity)
    distance = fling_distance(cfg, velocity)
    spline = Spline(cfg)
    ts, ps = [], []
    for i in range(steps + 1):
        t = i / steps
        ts.append(t * duration)
        ps.append(distance * spline.distance_coefficient(t))
    return ts, ps, duration, distance


def velocity_trace(cfg, velocity=VELOCITY, steps=400):
    """(times_ms, velocities_px_s) for one fling."""
    duration = fling_duration(cfg, velocity)
    distance = fling_distance(cfg, velocity)
    spline = Spline(cfg)
    ts, vs = [], []
    for i in range(steps + 1):
        t = i / steps
        ts.append(t * duration)
        vs.append(spline.velocity_coefficient(t) * distance / duration * 1000.0)
    return ts, vs


# ---------------------------------------------------------------------------
# Minimal SVG plotting helpers
# ---------------------------------------------------------------------------
class Svg:
    W, H = 780, 460
    ML, MR, MT, MB = 86, 24, 56, 62  # margins

    def __init__(self, title=None):
        self.parts = []
        self.parts.append(
            f'<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 {self.W} {self.H}" '
            f'font-family="{FONT}">')
        self.parts.append(f'<rect width="{self.W}" height="{self.H}" fill="#ffffff"/>')
        if title:
            self.text(self.W / 2, 30, title, size=17, weight="bold",
                      anchor="middle", color=PALETTE["text"])
        self.legend_y = 52

    @property
    def plot_w(self):
        return self.W - self.ML - self.MR

    @property
    def plot_h(self):
        return self.H - self.MT - self.MB

    def sx(self, x, x_max):
        return self.ML + (x / x_max) * self.plot_w

    def sy(self, y, y_max):
        return self.MT + self.plot_h - (y / y_max) * self.plot_h

    def text(self, x, y, s, size=13, weight="normal", anchor="start",
             color=PALETTE["text"], style=""):
        s = s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;")
        self.parts.append(
            f'<text x="{x:.1f}" y="{y:.1f}" font-size="{size}" '
            f'font-weight="{weight}" text-anchor="{anchor}" fill="{color}" '
            f'{style}>{s}</text>')

    def line(self, x1, y1, x2, y2, color, width=1.0, dash=None):
        d = f' stroke-dasharray="{dash}"' if dash else ""
        self.parts.append(
            f'<line x1="{x1:.1f}" y1="{y1:.1f}" x2="{x2:.1f}" y2="{y2:.1f}" '
            f'stroke="{color}" stroke-width="{width}"{d} stroke-linecap="round"/>')

    def polyline(self, pts, color, width=2.4):
        s = " ".join(f"{x:.1f},{y:.1f}" for x, y in pts)
        self.parts.append(
            f'<polyline points="{s}" fill="none" stroke="{color}" '
            f'stroke-width="{width}" stroke-linejoin="round" '
            f'stroke-linecap="round"/>')

    def rect(self, x, y, w, h, fill, opacity=1.0):
        self.parts.append(
            f'<rect x="{x:.1f}" y="{y:.1f}" width="{w:.1f}" height="{h:.1f}" '
            f'fill="{fill}" fill-opacity="{opacity}"/>')

    def dot(self, x, y, color, r=4.0):
        self.parts.append(
            f'<circle cx="{x:.1f}" cy="{y:.1f}" r="{r}" fill="{color}" '
            f'stroke="#ffffff" stroke-width="1.5"/>')

    def axes(self, x_max, y_max, x_label, y_label, x_fmt=None, y_fmt=None,
             x_ticks=5, y_ticks=4):
        if x_fmt is None:
            x_fmt = lambda v: f"{v:.3g}"  # noqa: E731
        if y_fmt is None:
            y_fmt = lambda v: f"{v:.3g}"  # noqa: E731
        x0, y0 = self.ML, self.MT + self.plot_h
        # grid + ticks
        for i in range(y_ticks + 1):
            v = y_max * i / y_ticks
            y = self.sy(v, y_max)
            self.line(x0, y, self.W - self.MR, y, PALETTE["grid"], 1.0)
            self.text(x0 - 10, y + 4, y_fmt(v), anchor="end", size=12,
                      color=PALETTE["gray"])
        for i in range(x_ticks + 1):
            v = x_max * i / x_ticks
            x = self.sx(v, x_max)
            self.line(x, self.MT, x, y0, PALETTE["grid"], 1.0)
            self.text(x, y0 + 20, x_fmt(v), anchor="middle", size=12,
                      color=PALETTE["gray"])
        # axis lines
        self.line(x0, self.MT, x0, y0, PALETTE["axis"], 1.6)
        self.line(x0, y0, self.W - self.MR, y0, PALETTE["axis"], 1.6)
        # axis labels
        self.text(self.W / 2, y0 + 44, x_label, anchor="middle", size=13.5,
                  weight="bold", color=PALETTE["text"])
        self.text(24, self.MT + self.plot_h / 2, y_label, anchor="middle",
                  size=13.5, weight="bold", color=PALETTE["text"],
                  style=f'transform="rotate(-90 24 {self.MT + self.plot_h / 2})"')

    def legend(self, entries):
        """entries: list of (label, color). Wraps to a new row at the right edge."""
        x = self.ML
        y = self.legend_y
        for label, color in entries:
            est = 33 + len(label) * 6.6 + 26
            if x + est > self.W - self.MR and x > self.ML:
                y += 22
                x = self.ML
            self.line(x, y, x + 26, y, color, 3.0)
            self.text(x + 33, y + 4.5, label, size=12.5, color=PALETTE["text"])
            x += est
        self.legend_y = y + 26

    def save(self, name):
        self.parts.append("</svg>")
        path = os.path.join(OUT_DIR, name)
        os.makedirs(OUT_DIR, exist_ok=True)
        with open(path, "w") as f:
            f.write("\n".join(self.parts))
        print(f"wrote {os.path.normpath(path)}")


def fmt_ms(v):
    return f"{v / 1000:.1f}s" if v >= 1000 else f"{v:.0f}ms"


def fmt_px(v):
    return f"{v / 1000:.0f}k" if v >= 1000 else f"{v:.0f}"


def sample_points(ts, ps, n=120):
    """Thin out a trace to n points for compact SVG output."""
    if len(ts) <= n:
        return list(zip(ts, ps))
    step = (len(ts) - 1) / (n - 1)
    return [(ts[int(i * step)], ps[int(i * step)]) for i in range(n)]


def plot_curves(name, title, traces, x_label, y_label, x_fmt, y_fmt,
                y_max=None, x_max=None):
    """traces: list of (label, color, ts, ps)."""
    svg = Svg(title)
    x_max = x_max or max(max(t) for _, _, t, _ in traces)
    y_max = y_max or max(max(p) for _, _, _, p in traces)
    x_max *= 1.02
    y_max *= 1.06
    svg.legend([(label, color) for label, color, _, _ in traces])
    svg.axes(x_max, y_max, x_label, y_label, x_fmt, y_fmt)
    for _, color, ts, ps in traces:
        pts = [(svg.sx(x, x_max), svg.sy(y, y_max)) for x, y in sample_points(ts, ps)]
        svg.polyline(pts, color)
    svg.save(name)


# ---------------------------------------------------------------------------
# 1. Pipeline diagram (hand-laid-out boxes + arrows)
# ---------------------------------------------------------------------------
def pipeline_diagram():
    svg = Svg("How one frame of a fling is computed")
    BOX = "#dbeafe"
    EDGE = "#0969da"
    SOFT = "#f6f8fa"
    SOFT_EDGE = "#d0d7de"

    def box(x, y, w, h, lines, fill=BOX, edge=EDGE, first_bold=True):
        svg.rect(x, y, w, h, fill, 1.0)
        svg.parts.append(
            f'<rect x="{x}" y="{y}" width="{w}" height="{h}" fill="none" '
            f'stroke="{edge}" stroke-width="1.4" rx="8"/>')
        lh = 19
        ty = y + h / 2 - (len(lines) - 1) * lh / 2 + 5
        for i, ln in enumerate(lines):
            weight = "bold" if (i == 0 and first_bold) else "normal"
            size = 14 if i == 0 else 12
            color = PALETTE["text"] if i == 0 else PALETTE["gray"]
            svg.text(x + w / 2, ty + i * lh, ln, size=size, weight=weight,
                     anchor="middle", color=color)

    def arrow(x1, y1, x2, y2, label=None):
        svg.line(x1, y1, x2, y2, PALETTE["axis"], 1.6)
        # arrowhead
        ang = math.atan2(y2 - y1, x2 - x1)
        for da in (2.6, -2.6):
            svg.line(x2, y2,
                     x2 - 9 * math.cos(ang + da), y2 - 9 * math.sin(ang + da),
                     PALETTE["axis"], 1.6)
        if label:
            mx, my = (x1 + x2) / 2, (y1 + y2) / 2 - 9
            svg.text(mx, my, label, size=11.5, anchor="middle",
                     color=PALETTE["gray"])

    row1 = 78
    box(40, row1, 260, 74, ["FlingConfiguration",
                            "10 physics & spline parameters",
                            "(you control these)"])
    box(370, row1, 230, 74, ["Density", "screen density, e.g. 2.625",
                             "(from LocalDensity)"],
        fill=SOFT, edge=SOFT_EDGE)

    row2 = 216
    box(150, row2, 360, 74, ["FlingCalculator",
                             "duration T and total distance D",
                             "for the initial velocity v₀"])

    row3 = 352
    box(60, row3, 250, 74, ["AndroidFlingSpline",
                            "precomputed lookup table",
                            "of numberOfSplinePoints samples"])
    box(430, row3, 290, 74, ["Every animation frame",
                             "position(t) = D · spline(t / T)",
                             "velocity(t) = spline′(t / T) · D / T"],
        fill="#e7f6ec", edge=PALETTE["green"])

    arrow(250, row1 + 74, 290, row2 - 2)
    arrow(470, row1 + 74, 430, row2 - 2)
    arrow(240, row2 + 74, 200, row3 - 2, "built once per configuration")
    arrow(430, row2 + 74, 500, row3 - 2)
    arrow(310, row3 + 37, 430, row3 + 37, "coefficient lookup")
    svg.save("fling-pipeline.svg")


# ---------------------------------------------------------------------------
# 2. One fling, annotated (position + velocity vs real time)
# ---------------------------------------------------------------------------
def annotated_fling():
    cfg = dict(DEFAULTS)
    ts, ps, duration, distance = position_trace(cfg)
    _, vs = velocity_trace(cfg)
    v_max_initial = vs[0]

    svg = Svg(f"Anatomy of one fling (default config, v₀ = 8000 px/s, 420 dpi)")
    svg.legend([("position (px, left axis)", PALETTE["blue"]),
                ("velocity (px/s, right axis)", PALETTE["orange"])])
    y_max_pos = distance * 1.08
    y_max_vel = v_max_initial * 1.08
    x_max = duration * 1.02
    svg.axes(x_max, y_max_pos, "time since finger lift", "position (px)",
             fmt_ms, fmt_px)

    # velocity scale on the right
    vx0 = svg.W - svg.MR
    for i in range(5):
        v = y_max_pos * i / 4
        y = svg.sy(v, y_max_pos)
        v_actual = v / y_max_pos * y_max_vel
        svg.text(vx0 + 10, y + 4, fmt_px(v_actual), anchor="start", size=12,
                 color=PALETTE["gray"])
    svg.text(vx0 + 10, svg.MT - 8, "px/s", anchor="start", size=12,
             color=PALETTE["gray"])

    # phase shading: first 10% of the duration carries most of the speed
    t_split = duration * DEFAULTS["splineInflection"] * 3
    svg.rect(svg.ML, svg.MT, svg.sx(t_split, x_max) - svg.ML, svg.plot_h,
             PALETTE["blue"], 0.05)
    svg.text(svg.ML + 8, svg.MT + 18, "high-speed glide", size=12.5,
             color=PALETTE["blue"], weight="bold")
    svg.text(svg.sx(t_split, x_max) + 8, svg.MT + 18, "deceleration tail",
             size=12.5, color=PALETTE["gray"], weight="bold")

    pts = [(svg.sx(x, x_max), svg.sy(y, y_max_pos)) for x, y in sample_points(ts, ps)]
    svg.polyline(pts, PALETTE["blue"])
    # velocity is rescaled onto the position axis; right-edge ticks give the true scale
    pts = [(svg.sx(x, x_max), svg.sy(y / y_max_vel * y_max_pos, y_max_pos))
           for x, y in sample_points(ts, vs)]
    svg.polyline(pts, PALETTE["orange"], width=2.0)

    # annotations
    y_end = svg.sy(distance, y_max_pos)
    svg.line(svg.ML, y_end, svg.W - svg.MR, y_end, PALETTE["gray"], 1.0,
             dash="4 4")
    svg.text(svg.W - svg.MR - 6, y_end - 8,
             f"total distance D = {distance / 1000:.1f}k px", anchor="end",
             size=12.5, color=PALETTE["text"])
    svg.text(svg.sx(duration, x_max) - 6, y_end - 28,
             f"duration T = {duration:.0f} ms", anchor="end", size=12.5,
             color=PALETTE["text"])
    svg.dot(svg.sx(0, x_max), svg.sy(ps[0], y_max_pos), PALETTE["orange"])
    svg.text(svg.sx(0, x_max) + 10, svg.sy(ps[0], y_max_pos) + 20,
             f"starts at v₀ = {v_max_initial:.0f} px/s", size=12.5,
             color=PALETTE["orange"])
    svg.save("fling-anatomy.svg")


# ---------------------------------------------------------------------------
# 3. Parameter-effect plots (real physics, one parameter varied)
# ---------------------------------------------------------------------------
def scroll_friction_plot():
    traces = []
    for value, color in ((0.004, PALETTE["green"]), (0.008, PALETTE["blue"]),
                         (0.04, PALETTE["red"])):
        cfg = dict(DEFAULTS, scrollViewFriction=value)
        ts, ps, duration, distance = position_trace(cfg)
        traces.append((f"scrollViewFriction = {value}", color, ts, ps))
        print(f"  scrollFriction={value}: T={duration:.0f}ms D={distance:.0f}px")
    plot_curves("scroll-friction.svg", "scrollViewFriction: position over time "
                "(v₀ = 8000 px/s, 420 dpi)", traces,
                "time since finger lift", "position (px)", fmt_ms, fmt_px)


def deceleration_friction_plot():
    traces = []
    for value, color in ((0.02, PALETTE["green"]), (0.09, PALETTE["blue"]),
                         (0.5, PALETTE["red"])):
        cfg = dict(DEFAULTS, decelerationFriction=value)
        ts, vs = velocity_trace(cfg)
        traces.append((f"decelerationFriction = {value}", color, ts, vs))
        print(f"  decelFriction={value}: T={fling_duration(cfg):.0f}ms "
              f"D={fling_distance(cfg):.0f}px")
    plot_curves("deceleration-friction.svg", "decelerationFriction: velocity "
                "over time (v₀ = 8000 px/s, 420 dpi)", traces,
                "time since finger lift", "velocity (px/s)", fmt_ms, fmt_px)


def deceleration_rate_plot():
    traces = []
    for value, color in ((1.5, PALETTE["green"]),
                         (DEFAULTS["decelerationRate"], PALETTE["blue"]),
                         (4.0, PALETTE["red"])):
        cfg = dict(DEFAULTS, decelerationRate=value)
        ts, ps, duration, distance = position_trace(cfg)
        label = f"decelerationRate = {value:.2f}" + ("  (default)" if abs(value - DEFAULTS["decelerationRate"]) < 1e-3 else "")
        traces.append((label, color, ts, ps))
        print(f"  decelRate={value:.2f}: T={duration:.0f}ms D={distance:.0f}px")
    plot_curves("deceleration-rate.svg", "decelerationRate: position over time "
                "(v₀ = 8000 px/s, 420 dpi)", traces,
                "time since finger lift", "position (px)", fmt_ms, fmt_px)


def gravity_plot():
    traces = []
    for value, color in ((3.0, PALETTE["green"]),
                         (DEFAULTS["gravitationalForce"], PALETTE["blue"]),
                         (18.0, PALETTE["red"])):
        cfg = dict(DEFAULTS, gravitationalForce=value)
        ts, ps, duration, distance = position_trace(cfg)
        label = f"gravitationalForce = {value:g}" + ("  (default)" if abs(value - DEFAULTS["gravitationalForce"]) < 1e-3 else "")
        traces.append((label, color, ts, ps))
    plot_curves("gravity.svg", "gravitationalForce: position over time "
                "(v₀ = 8000 px/s, 420 dpi)", traces,
                "time since finger lift", "position (px)", fmt_ms, fmt_px)


# ---------------------------------------------------------------------------
# 4. Spline-shape plots (normalized coefficient space)
# ---------------------------------------------------------------------------
def spline_shape():
    cfg = dict(DEFAULTS)
    spline = Spline(cfg)
    inflection = cfg["splineInflection"]
    ts = [i / 300 for i in range(301)]
    ps = [spline.distance_coefficient(t) for t in ts]

    svg = Svg("The normalized spline curve (default parameters)")
    svg.legend([("position coefficient D(t)", PALETTE["blue"])])
    svg.axes(1.0, 1.06, "normalized time  t / T", "normalized distance  D(t)",
             lambda v: f"{v:.1f}", lambda v: f"{v:.1f}")

    # control guides: the two "tension lines" the Bezier blends between
    y_st = svg.sy(DEFAULTS["splineStartTension"], 1.06)
    svg.line(svg.ML, y_st, svg.W - svg.MR, y_st, PALETTE["gray"], 1.0,
             dash="4 4")
    svg.text(svg.W - svg.MR - 6, y_st - 7,
             f"splineStartTension = {cfg['splineStartTension']}", anchor="end",
             size=12, color=PALETTE["gray"])

    pts = [(svg.sx(t, 1.0), svg.sy(p, 1.06)) for t, p in zip(ts, ps)]
    svg.polyline(pts, PALETTE["blue"])

    # mark the inflection region and the fast-start/slow-end split
    t_inf = inflection
    x_inf = svg.sx(t_inf, 1.0)
    svg.line(x_inf, svg.MT, x_inf, svg.MT + svg.plot_h, PALETTE["red"], 1.2,
             dash="5 4")
    svg.text(x_inf + 8, svg.MT + 16, "splineInflection", size=12.5,
             color=PALETTE["red"], weight="bold")
    svg.text(x_inf + 8, svg.MT + 33, "start-tension shape ends,", size=12,
             color=PALETTE["gray"])
    svg.text(x_inf + 8, svg.MT + 49, "end-tension shape takes over", size=12,
             color=PALETTE["gray"])
    svg.dot(pts[0][0], pts[0][1], PALETTE["blue"])
    svg.text(pts[0][0] + 10, pts[0][1] - 10,
             "initial slope ≈ initial velocity", size=12, color=PALETTE["blue"])
    svg.save("spline-shape.svg")


def spline_inflection_plot():
    traces = []
    for value, color in ((0.1, PALETTE["blue"]), (0.2, PALETTE["orange"]),
                         (0.4, PALETTE["red"])):
        cfg = dict(DEFAULTS, splineInflection=value)
        spline = Spline(cfg)
        ts = [i / 200 for i in range(201)]
        ps = [spline.distance_coefficient(t) for t in ts]
        label = f"splineInflection = {value}" + ("  (default)" if value == 0.1 else "")
        traces.append((label, color, ts, ps))
    plot_curves("spline-inflection.svg",
                "splineInflection: normalized spline curve", traces,
                "normalized time  t / T", "normalized distance  D(t)",
                lambda v: f"{v:.1f}", lambda v: f"{v:.1f}")


def start_tension_plot():
    traces = []
    for value, color in ((0.02, PALETTE["green"]), (0.1, PALETTE["blue"]),
                         (0.5, PALETTE["red"])):
        cfg = dict(DEFAULTS, splineStartTension=value)
        spline = Spline(cfg)
        ts = [i / 200 for i in range(201)]
        ps = [spline.distance_coefficient(t) for t in ts]
        label = f"splineStartTension = {value}" + ("  (default)" if value == 0.1 else "")
        traces.append((label, color, ts, ps))
    plot_curves("start-tension.svg",
                "splineStartTension: normalized spline curve", traces,
                "normalized time  t / T", "normalized distance  D(t)",
                lambda v: f"{v:.1f}", lambda v: f"{v:.1f}")


def end_tension_plot():
    traces = []
    for value, color in ((0.5, PALETTE["green"]), (1.0, PALETTE["blue"]),
                         (1.8, PALETTE["red"])):
        cfg = dict(DEFAULTS, splineEndTension=value)
        spline = Spline(cfg)
        ts = [i / 200 for i in range(201)]
        ps = [spline.distance_coefficient(t) for t in ts]
        label = f"splineEndTension = {value}" + ("  (default)" if value == 1.0 else "")
        traces.append((label, color, ts, ps))
    plot_curves("end-tension.svg",
                "splineEndTension: normalized spline curve", traces,
                "normalized time  t / T", "normalized distance  D(t)",
                lambda v: f"{v:.1f}", lambda v: f"{v:.1f}")


def spline_points_plot():
    svg = Svg("numberOfSplinePoints: resolution of the lookup table")
    svg.legend([("100 points (default)", PALETTE["blue"]),
                ("25 points", PALETTE["orange"]),
                ("10 points", PALETTE["red"])])
    svg.axes(1.0, 1.06, "normalized time  t / T", "normalized distance  D(t)",
             lambda v: f"{v:.1f}", lambda v: f"{v:.1f}")
    fine_ts = [i / 300 for i in range(301)]
    for n, color, width in ((100, PALETTE["blue"], 2.6),
                            (25, PALETTE["orange"], 2.0),
                            (10, PALETTE["red"], 2.0)):
        cfg = dict(DEFAULTS, numberOfSplinePoints=n)
        spline = Spline(cfg)
        ps = [spline.distance_coefficient(t) for t in fine_ts]
        pts = [(svg.sx(t, 1.0), svg.sy(p, 1.06)) for t, p in zip(fine_ts, ps)]
        svg.polyline(pts, color, width)
        if n != 100:
            # show the actual sample knots
            knot_ts = [i / n for i in range(n + 1)]
            for kt in knot_ts:
                svg.dot(svg.sx(kt, 1.0),
                        svg.sy(spline.distance_coefficient(kt), 1.06),
                        color, r=2.6)
    svg.save("spline-points.svg")


# ---------------------------------------------------------------------------
if __name__ == "__main__":
    print(f"default decelerationRate = {DEFAULTS['decelerationRate']:.6f}")
    d0 = fling_duration(DEFAULTS)
    dist0 = fling_distance(DEFAULTS)
    print(f"default fling @8000px/s, 420dpi: T={d0:.0f}ms D={dist0:.0f}px")
    _, vs0 = velocity_trace(DEFAULTS, steps=1)
    print(f"initial spline velocity = {vs0[0]:.0f} px/s (input was 8000)")

    pipeline_diagram()
    annotated_fling()
    scroll_friction_plot()
    deceleration_friction_plot()
    deceleration_rate_plot()
    gravity_plot()
    spline_shape()
    spline_inflection_plot()
    start_tension_plot()
    end_tension_plot()
    spline_points_plot()
    print("done")
