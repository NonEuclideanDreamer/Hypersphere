# Hypersphere
Depicting objects from within the 3-Sphere

Main Classes: 
ForwardVideo: Creates an image sequence depicting the observer flying through the Cellcomplex subject (instantanuous light, but som nice blur-effect)
FiniteSpeed: Now the speed of light c is finite, measured as factor of the observers speed

classes called in these:
Spherpoint: java class of point in the 3-dimensional sphere
Spherline: java class of geodesics, defined by a point and a (4d-)vector
E4: java class of 4-d vectors
Observer: java class for the camera. Defined by a geodesic and a (4d-)vector denoting which direction is "right"
Cellcomplex: java class of 2-d cell complexes in the 3 Sphere, defined by the vertices, and an array spesifying which vertices form a face
Polychoron: gathering of methods implementing differen uniform polycora (*Haven't finished refactoring the degenerate ones...)

Feel free to ask, if there are any questions, for example in my discord: https://discord.gg/zaFUHj282
