package com.dallaway.ga

import scala.util.Random
import cats.Show
import cats.implicits._

case class MetaParams(
    popSize: Int,
    chromosomeLength: Int,
    crossoverRate: Double,
    mutationRate: Double,
)

case class Individual(bits: Vector[Boolean]) extends AnyVal
case class Population(individuals: Vector[Individual]) extends AnyVal

object Population {
  def random(params: MetaParams): Population =
    Population(Vector.fill(params.popSize)(Individual.random(params)))
}

object Individual {
  def random(params: MetaParams): Individual =
    Individual(Vector.fill(params.chromosomeLength)(Random.nextBoolean)) // TODO: Rand[T]

  implicit val show: Show[Individual] = i =>
    i.bits.map(b => if (b) "1" else "0").mkString
}

object GA {

  type PopulationFitness = Vector[Int]

  def evaluate(
      population: Population,
      f: Individual => Int): PopulationFitness =
    population.individuals.map(f)

  // roulette-wheel sampling
  def select(
      pop: Population,
      fitness: PopulationFitness): (Individual, Individual) = {

    // Dtop 1 to remove the initial zero
    val accumulated_fitness = fitness.scanLeft(0) { _ + _ }.drop(1)
    val total_fitness = accumulated_fitness.last

    val wheel = accumulated_fitness zip pop.individuals

    def select1(): Individual = {
      val number = Random.nextInt(total_fitness)
      wheel
        .dropWhile { case (fitness, _) => fitness < number }
        .map { case (_, individual) => individual }
        .head
    }

    (select1(), select1())
  }

  def crossover(mum: Individual, dad: Individual): (Individual, Individual) = {
    val locus = Random.nextInt(mum.bits.length)

    val mum_bits = mum.bits.splitAt(locus)
    val dad_bits = dad.bits.splitAt(locus)

    val child1 = Individual(mum_bits._1 ++ dad_bits._2)
    val child2 = Individual(dad_bits._1 ++ mum_bits._2)

    (child1, child2)
  }

  def mutator(rate: Double): Individual => Individual =
    individual =>
      Individual(
        individual.bits.map(bit =>
          if (Random.nextFloat() <= rate) !bit else bit)
    )

  def evolve(
      params: MetaParams,
      pop: Population,
      fitness: PopulationFitness): Population = {

    val mutate = mutator(params.mutationRate)

    def create2children(): (Individual, Individual) = {
      val (mum, dad) = select(pop, fitness)
      val (child1, child2) =
        if (Random.nextDouble() <= params.crossoverRate) crossover(mum, dad)
        else (mum, dad)
      (mutate(child1), mutate(child2))
    }

    // TODO: handle odd popSize
    Population(
      Vector
        .fill(params.popSize / 2)(create2children)
        .collect { case (c1, c2) => Vector(c1, c2) }
        .flatten)
  }

}

object Main {
  def main(args: Array[String]): Unit = {

    val params = MetaParams(
      popSize = 100,
      chromosomeLength = 20,
      crossoverRate = 0.7,
      mutationRate = 0.001,
    )

    val fitness: Individual => Int =
      _.bits.filter(identity).length

    val solved: GA.PopulationFitness => Boolean =
      _.exists(score => score == params.chromosomeLength)

    val verbose = false

    @scala.annotation.tailrec
    def iterate(pop: Population, gen: Int = 0): (Population, Int) = {
      val scores = GA.evaluate(pop, fitness)

      if (verbose) {
        (pop.individuals.map(_.show) zip scores).foreach(println)
      }

      solved(scores) match {
        case true => (pop, gen)
        case false => iterate(GA.evolve(params, pop, scores), gen + 1)
      }
    }

    println("Run,Generation")
    for (run <- 1 to 20) {
      val initialPopulation = Population.random(params)
      val (_, gen) = iterate(initialPopulation)
      println(s"$run,$gen")
    }

  }
}
