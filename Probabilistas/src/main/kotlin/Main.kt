/**
 * Implementation and tests of an algorithm that use random pivots and recursive function to found
 * on a disordered list a number at a know position when the list is sorted.
 * Example: Found 5 at list [2,8,3,1,4,7,9] (Sorted: [1,2,3,4,7,8,9]). Solution: 7
 *
 * @author Roberto CCU
 */

import java.lang.Math.abs
import java.util.*

fun main(args : Array<String>) {
    val randomizer : Random = Random()

    // Params
    var sliceAverageTime : MutableList<Long> = mutableListOf()
    val slices : Int = 9 // Portions of the list where test K multiple times.
    val listSize : Int = 100 * slices
    val repeats : Int = 10000

    // To show statics
    var trues : Int = 0
    var falses : Int = 0

    // To show progress
    var count = 0.0
    var total = repeats*slices

    for (slice in 0..slices-1) {
        var sliceTimes : MutableList<Long> = mutableListOf()

        // Execute algorithm with "repeats" different k param inside of the portion.
        for (i in 1..repeats) {
            var kIndex : Int = (abs(randomizer.nextInt(listSize/slices)+1)) + (slice*(listSize/slices))
            System.out.println("k: $kIndex")

            var startTime = System.nanoTime()
            var result: Boolean = numAtKTest(listSize, kIndex) // Executing Algorithm with a new random list of specified size
            var endTime = System.nanoTime()

            sliceTimes.add(endTime-startTime)

            count++
            System.out.println("[COUNT] $count (${(count/total)*100} %)")

            when (result) {
                true -> trues++
                false -> falses++
            }
            System.out.println("[RESULT] $result")
        }

        var sum : Long = 0
        for (time in sliceTimes) {
            sum += time
        }
        sliceAverageTime.add(sum/sliceTimes.size)
    }

    System.out.println("[TEST RESULT] true: $trues | false: $falses")

    var i = 1
    for (time in sliceAverageTime) { // Calculating Average time per "k portion"
        System.out.println("Slice $i: $time ns (${time/1000000.0} ms)")
        i++
    }
}

/**
 * Creates a random list and executes the algoritm with it.
 * @param listSize: Size of the random list.
 * @param kIndex: Index (at sorted list) to search.
 * @retun true if the result of the algorithm is the correct.
 */
fun numAtKTest(listSize:Int, kIndex:Int): Boolean {
    val randomizer : Random = Random()
    var numbers : MutableList<Int> = mutableListOf()
    //System.out.println("Creating random list with $listSize numbers...")
    for (num in 1..listSize) {
        numbers.add(abs(randomizer.nextInt(listSize*5)))
    }

    //System.out.println("Sorting list...")
    val sortedList : List<Int> = numbers.sorted()

    //System.out.println("Searching element $kIndex on random list...")
    val result: Int = numAtK(numbers,kIndex,false)

    val expected: Int = sortedList[kIndex-1]
    System.out.println("Found: $result | Expected: $expected")
    return result == expected
}

/**
 * Algorithm that found in a disordered list of integers, the number at index "k" when the list is
 * sorted. Without sorting.
 * @param S: Disordered list of Integers.
 * @param k: Index (at sorted S) of the number to found.
 * @param debug: When true, show messages on System output.
 * @return the integer found.
 */
fun numAtK(S:MutableList<Int>, k:Int, debug:Boolean = false) : Int {
    val randomizer : Random = Random()
    val randPos : Int = randomizer.nextInt(S.size)
    val numAtRand : Int = S[randPos] // Element in random position of the list (Pivot)
    S.removeAt(randPos) // Removing pivot from the list
    if (debug) {System.out.println("Random number selected is $numAtRand at position $randPos")}

    var greaterThanRand : MutableList<Int> = mutableListOf()
    var lessThanRand : MutableList<Int> = mutableListOf()
    for (num in S) {
        when {
            num <= numAtRand -> lessThanRand.add(num)
            num > numAtRand -> greaterThanRand.add(num)
        }
    }

    if (debug) {
        System.out.print("lessThanRand: ")
        for (num in lessThanRand) {
            System.out.print("$num ")
        }
        System.out.print("\nPivot: $numAtRand")
        System.out.print("\ngreaterThanRand: ")
        for (num in greaterThanRand) {
            System.out.print("$num ")
        }
    }

    if (lessThanRand.size == k-1) { // The random pivot is the element at K index if the list is sorted
        if (debug) {System.out.println("\nFound! $numAtRand")}
        return numAtRand

    } else if (lessThanRand.size < k-1) { // The element at K (when sorted) is greater than pivot
        if (debug) {System.out.println("\nK element is greater than $numAtRand; repeating with S = ${greaterThanRand.size} and k = ${k-(lessThanRand.size + 1)}")}
        return numAtK(greaterThanRand,k-(lessThanRand.size + 1), debug)

    } else if (lessThanRand.size >= k) { // The element at K (when sorted) is less than pivot
        if (debug) {System.out.println("\nK element is less than $numAtRand; repeating with S = ${lessThanRand.size} and k = $k") }
        return numAtK(lessThanRand, k, debug)
    }
    return -1
}