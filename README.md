# Basic GA

Implementation of a basic GA from the first
exercise in _An Introduction to Genetic Algorithms_ by Melanie Mitchell (p. 31):

> "Implement a simple GA with fitness-proportionate selection,
roulette-wheel sampling, population size 100, single-point crossover
rate p_c = 0.7, and bitwise mutation rate p_m = 0.001.
Try it on the following fitness function: f(x) = number of ones in x,
where x is a chromosome of length 20.
Perform 20 runs, and measure the average generation at which the string of all ones is discovered."

## Example run:

By default 20 runs are made, and the output shows which generation the solution was found in:

```
$ sbt run
Run,Generation
1,22
2,72
3,11
4,22
5,20
6,27
7,206
8,21
9,26
10,22
11,34
12,26
13,27
14,34
15,39
16,31
17,36
18,47
19,22
20,52
```

Verbose output (flag in the source) shows the details of every generation in every run.

## Optimised run

Step 1 is to package the application with `sbt assembly`.

Step 2: check you can run the JAR file, e.g.,:

```
$ time java -jar target/scala-2.12/basic-assembly-0.1.0-SNAPSHOT.jar
Run,Generation
1,30
2,39
3,20
4,12
5,13
6,35
7,47
8,35
9,17
10,21
11,23
12,26
13,33
14,39
15,37
16,16
17,32
18,22
19,24
20,22

real	0m1.250s
user	0m2.364s
sys	0m0.173s
```

Step 3: assuming you've installed GraalVM, run:

```
$ native-image -jar target/scala-2.12/basic-assembly-0.1.0-SNAPSHOT.jar
```

This will take a few minutes, and will give you a file called `basic-assembly-0.1.0-SNAPSHOT`.

Step 4: run the binary

```
$ time ./basic-assembly-0.1.0-SNAPSHOT
Run,Generation
1,17
2,23
3,123
4,30
5,17
6,38
7,26
8,21
9,19
10,36
11,28
12,23
13,46
14,104
15,19
16,38
17,6
18,20
19,20
20,33

real	0m0.812s
user	0m0.634s
sys	0m0.147s
```
