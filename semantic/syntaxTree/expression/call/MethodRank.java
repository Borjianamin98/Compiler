package semantic.syntaxTree.expression.call;

import semantic.syntaxTree.declaration.method.Argument;

import java.util.Comparator;
import java.util.List;

/**
 * call and func must have same length of parameters/arguments
 * each parameter must be convertible(widenable) to function argument
 */
public class MethodRank {
    public static final Comparator<MethodRank> comparator = Comparator.comparing(MethodRank::getSumOfDiffLevel)
            .thenComparing(MethodRank::getMaxLevel);

    private int overloadMethodIndex;
    /**
     * sum of difference between each method argument and call parameters
     * call func(par1, par2, ...) and func(arg1, arg2, ...)
     * sumOfDiffLevel = Diff(par1, arg1) + Diff(par2, arg2) + ...
     * precondition:
     */
    private int sumOfDiffLevel;

    /**
     * max diffLevel between arguments of function and parameters of call
     * max(Diff(par1, arg1), Diff(par2, arg2), ...)
     */
    private int maxLevel;

    /**
     * arguments of function
     */
    private List<Argument> arguments;

    public MethodRank(List<Argument> arguments, int overloadMethodIndex) {
        this.arguments = arguments;
        this.overloadMethodIndex = overloadMethodIndex;
    }

    public void addSumOfDiffLevel(int value) {
        sumOfDiffLevel += value;
        maxLevel = Math.max(maxLevel, value);
    }

    private int getSumOfDiffLevel() {
        return sumOfDiffLevel;
    }

    private int getMaxLevel() {
        return maxLevel;
    }

    public int getOverloadMethodIndex() {
        return overloadMethodIndex;
    }

    public List<Argument> getArguments() {
        return arguments;
    }
}
