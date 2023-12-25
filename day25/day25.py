import networkx as nx
import matplotlib.pyplot as plt

G = nx.Graph()
with open("input25.txt") as f:
    for line in f:
        parts = line.rstrip().split(": ")
        for v in parts[1].split(" "):
            G.add_edge(parts[0], v)

G.remove_edges_from(nx.minimum_edge_cut(G))

result = 1
for component in nx.connected_components(G):
    result *= len(component)

print(result)
