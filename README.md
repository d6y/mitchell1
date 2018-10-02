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

TODO

