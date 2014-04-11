/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package theorticalcalculations;
import java.util.*;
/**
 *
 * @author Tejashree
 */
public class TheorticalCalculations {

    public static double fact(int p) {
        if (p <= 1) {
            return 1;
        } else {
            double prod = 1;
            for (int i = 2; i <= p; i++) {
                prod *= i;
            }
            return prod;
        }
    }

    public static double [] calTheorticalProb(double lambda1, double lambda2,
            double mu, int m, int l, int k) {
        double prob[] = new double[k+1];
        double sum1 = 0;
        double sum2 = 0;
        double sum3 = 0;
        
        if (l <= m && m <= k) //case 1
        {
            for (int i = 1; i <= l; i++) //sum 1
            {
                sum1 += Math.pow(((lambda1 + lambda2) / mu), (double) i) / fact(i);
            }
            for (int i = l + 1; i <= m; i++) //sum2
            {
                sum2 += (Math.pow((lambda1 + lambda2), (double) l) / Math.pow(mu, (double) i)) * Math.pow(lambda1, (double) (i - l)) / fact(i);
            }
            for (int i = m + 1; i <= k; i++) //sum2
            {
                sum3 += (Math.pow((lambda1 + lambda2), (double) l) / Math.pow(mu, (double) i)) * Math.pow(lambda1, (double) (i - l)) / (fact(m) * Math.pow((double) m, (double) (i - m)));
            }
            // calculating P[0]
            
            prob[0] = (double) (1 / (1 + (sum1) + (sum2) + (sum3)));

            // calculating other probabilities
            for (int i = 1; i <= k; i++) {
                if (i <= l) {
                    prob[i] = Math.pow(((lambda1 + lambda2) / mu), (double) i) * prob[0] / fact(i);
                } else if (l < i && i <= m) {
                    prob[i] = (Math.pow((lambda1 + lambda2), (double) l) / Math.pow(mu, (double) i)) * Math.pow(lambda1, (double) (i - l)) * prob[0] / fact(i);

                } else if (m < i && i <= k) {
                    prob[i] = (Math.pow((lambda1 + lambda2), (double) l) / Math.pow(mu, (double) i)) * Math.pow(lambda1, (double) (i - l)) * prob[0] / (fact(m) * Math.pow((double) m, (double) (i - m)));
                }
            }
        }//case 1 ends
        else if (m <= l && l <= k) //case 2
        {
            for (int i = 1; i <= m; i++) //sum 1
            {
                sum1 += Math.pow((lambda1 + lambda2) / mu, (double) i) / fact(i);
            }
            for (int i = m + 1; i <= l; i++) //sum2
            {
                sum2 += Math.pow((lambda1 + lambda2) / mu, (double) i) / (fact(m) * Math.pow(m, (i - m)));
            }
            for (int i = l + 1; i <= k; i++) //sum2
            {
                sum3 += Math.pow((lambda1 + lambda2), (double) l) * Math.pow(lambda1, (double) (i - l)) / (Math.pow(mu, (double) i) * fact(m) * Math.pow(m, (i - m)));
            }
            // calculating P[0]
            prob[0] = (double) (1 / (1 + (sum1) + (sum2) + (sum3)));

            // calculating other probabilities
            for (int i = 1; i <= k; i++) {
                if (i <= m) {
                    prob[i] = Math.pow((lambda1 + lambda2) / mu, (double) i) * prob[0] / fact(i);

                } else if (m < i && i <= l) {
                    prob[i] = Math.pow((lambda1 + lambda2) / mu, (double) i) * prob[0] / (fact(m) * Math.pow(m, (i - m)));

                } else if (l < i && i <= k) {
                    prob[i] = Math.pow((lambda1 + lambda2), (double) l) * Math.pow(lambda1, (double) (i - l)) * prob[0] / (Math.pow(mu, (double) i) * fact(m) * Math.pow(m, (i - m)));

                }
            }

        }//case 2 ends

        return prob;
    }

    public static double calTheorticalMean(double prob[], int k) {
        double mean = 0;
        for (int i = 0; i <= k; i++) {
            mean += i * prob[i];
        }
        return mean;
    }

    public static double calLambdaEff(double prob[], double lambda1, double lambda2, int l, int k) {
        double lambdaEff = 0;
        for (int i = 1; i < k; i++) {
            if (i <= l) {
                lambdaEff += (lambda1 + lambda2) * prob[i];
            } else if (i > l) {
                lambdaEff += lambda1 * prob[i];
            }
        }
        return lambdaEff;
    }
    
    public static double calPBlockingAdmin(double prob[], int k)
    {
        return prob[k];
    }
    
    public static double calPBlockingUser(double prob[], double lambda1, double lambda2, int k, int l )
    {
        double arrivalBlockingProb = 0;
        for(int i = l; i <= k ; i++){
            arrivalBlockingProb += (lambda2) * prob[i];            
        }
        return arrivalBlockingProb / lambda2;
    }

    public static void main(String[] args) {
        // TODO code application logic here
        Scanner key = new Scanner(System.in);        
        System.out.println("Enter lambda 2 :");
        double lambda2 = key.nextDouble();
        System.out.println("Enter mu :");
        double mu = key.nextDouble();
        System.out.println("Enter m :");
        int m = key.nextInt();
        System.out.println("Enter l :");
        int l = key.nextInt();
        System.out.println("Enter k :");
        int k = key.nextInt();        
        System.out.println("Enter rho :");
        double rho[] = {0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.7, 0.8, 0.9, 1.0};
        double lambda1;
        
        for(int i = 0; i <rho.length ; i++ ){
            
            lambda1 = rho[i] * m * mu ;
            double prob[] = calTheorticalProb(lambda1,lambda2,mu, m, l, k);
            System.out.println("rho : " + rho[i]);
            double mean = calTheorticalMean(prob, k);
            System.out.println("The expected number of customers (analysis) : " 
                                + mean );
            double lambdaEff = calLambdaEff(prob, lambda1, lambda2, l, k);
            System.out.println("The expected waiting time of a customer (analysis) : " + mean / lambdaEff );
            System.out.println("Probability of User Blocking : " + calPBlockingUser(prob, lambda1, lambda2, k, l ));
            System.out.println("Probability of Admin Blocking : " + calPBlockingAdmin(prob, k));
        }
                
                
    }

}
