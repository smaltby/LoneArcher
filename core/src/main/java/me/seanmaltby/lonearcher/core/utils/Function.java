package me.seanmaltby.lonearcher.core.utils;

/**
 * Represents a function that accepts one argument and produces a result.
 * @param <T>	argument type
 * @param <R>	result type
 */
public interface Function<T, R>
{
	/**
	 * Applies this function to the given argument.
	 * @param t 	argument
	 * @return		result
	 */
	public R apply(T t);
}
