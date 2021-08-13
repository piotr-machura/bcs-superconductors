#ifndef _CONF_H
#define _CONF_H
#include<stdbool.h>

#define K_BOLTZMANN 1.0
#define G_COEFF 1.0
#define H_BAR 1.0
#define MASS 1.0
#define PI 3.141592653589793

#define DELTA_PRECISION 1e-6
#define DELTA_ZERO 1e-4
#define UNDER_SQRT_ZERO 1e-4

#define D_X 1e-3
#define N_X 100.0

#define GRIDSIZE N_X
#define DELTA_GUESS 1.0


#define MU_INITIAL 1e-6
#define MU_MIXING 0.25

#define ALPHA_MIXING 0.25
#define ALPHA_MIXING_THRESHOLD 100

#define T_INITIAL 1e-6
#define T_MULTIPLIER 1.01
#define T_MAX_STEPS ((int) 1e7)

#define OUTPUT_FILE "res.txt"

#endif
