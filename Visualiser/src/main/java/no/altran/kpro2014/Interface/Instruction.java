package no.altran.kpro2014.Interface;

/**
 * V2 interface for instructions
 * @author Stig Lau
 * @since June 2012
 */
public interface Instruction extends Comparable{

    long start();

    long length();
}
