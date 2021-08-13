#ifndef _CALC_H
#define _CALC_H
#include<stdint.h>

typedef struct {
    double delta;
    double n_particles;
    uint64_t iterations;
} conv_outcome;

double epsilon_k(double k);
double u_k2(double k, double e_k, double mu);
double v_k2(double k, double e_k, double mu);
double u_k_v_k(double k, double e_k, double mu);
double e_k(double delta, double k, double mu);
double fermi_dirac(double e, double mu, double t);
conv_outcome converge_delta(double mu, double t);
double grid(uint64_t i);


#endif
