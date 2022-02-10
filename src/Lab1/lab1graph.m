s = [1 1 2 2 2 3 3];
t = [1 2 2 3 4 2 5];
G = digraph(s,t);
h = plot(G);
labelText1 = {'q0', 'q1', 'q2', 'q3', 'q4'};
labelText2 = {'a', 'b', 'c', 'a', 'd', 'a', 'b'};
labelnode(h,[1 2 3 4 5],labelText1)
labeledge(h,s,t,labelText2)
highlight(h,[4 5], 'NodeColor', 'g');