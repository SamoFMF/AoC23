# AoC 2023 Day 24 - Part 2

from z3 import *

P = []
V = []
with open("input.txt") as f:
    for line in f:
        parts = line.split(" @ ")
        P += [[int(p) for p in parts[0].split(", ")]]
        V += [[int(p) for p in parts[1].split(", ")]]

x = Int('x')
y = Int('y')
z = Int('z')
dx = Int('dx')
dy = Int('dy')
dz = Int('dz')
T = [Int(f't{i}') for i in range(len(P))]

solver = Solver()

for t in T:
    solver.add(t > 0)

# Uncomment these restrictions to make assumptions that speed up the solve
# solver.add(dx > -500)
# solver.add(dx < 500)
# solver.add(dy > -500)
# solver.add(dy < 500)
# solver.add(dz > -500)
# solver.add(dz < 500)

for i in range(len(P)):
    t = T[i]
    p = P[i]
    v = V[i]
    solver.add(x + t * dx == p[0] + t * v[0])
    solver.add(y + t * dy == p[1] + t * v[1])
    solver.add(z + t * dz == p[2] + t * v[2])

solver.check()
model = solver.model()

print(f"dx={model.eval(dx)},dy={model.eval(dy)},dz={model.eval(dz)}")
print(model.eval(x+y+z))
