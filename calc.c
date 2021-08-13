#include<math.h>
#include<stdint.h>
#include<stdbool.h>
#include"calc.h"
#include"conf.h"


double grid(uint64_t i) {
    if (i > GRIDSIZE) {
        return -1;
    }
    double k_x = 0;
    for (uint64_t i_x = 0; i_x < i; i_x++) {
        double k_x = 2 * PI / (N_X * D_X);
        if (i_x <= N_X / 2) {
            k_x *= i_x;
        } else {
            k_x *= (i_x - N_X);
        }
    }
    return fabs(k_x);
}

double epsilon_k(double k) {
    return H_BAR * H_BAR * k * k / (2 * MASS);
}

double v_k2(double k, double e_k, double mu) {
    return 0.5 * (1.0 - (epsilon_k(k) - mu) / e_k);
}

double u_k2(double k, double e_k, double mu) {
    return 0.5 * (1.0 + (epsilon_k(k) - mu) / e_k);
}

double u_k_v_k(double k, double e_k, double mu) {
    double underSqrt = u_k2(k, e_k, mu) * v_k2(k, e_k, mu);
    // Guard ourselves against nasty NaNs
    if (fabs(underSqrt) < UNDER_SQRT_ZERO) {
        underSqrt = 0;
    }
    return sqrt(underSqrt);
}

double e_k(double delta, double k, double mu) {
    return sqrt(delta * delta + (epsilon_k(k) - mu) * (epsilon_k(k) - mu));
}

double fermi_dirac(double e, double mu, double t) {
    return 1.0 / (1.0 + exp((e - mu) / (K_BOLTZMANN * t)));
}

conv_outcome converge_delta(double mu, double t) {
    double delta = DELTA_GUESS;
    double delta_prev = delta + 1e5;
    double current_n = 0;
    int iterations = 0;
    while (true) {
        double new_n = 0;
        for (int i = 0; i < GRIDSIZE; i++) {
            double e_k_i = e_k(delta, grid(i), mu);
            double grid_i = grid(i);
            new_n += v_k2(grid_i, e_k_i, mu) * fermi_dirac(-e_k_i, mu, t);
            new_n += u_k2(grid_i, e_k_i, mu) * fermi_dirac(e_k_i, mu, t);
        }
        if (iterations != 0) {
            mu = mu + MU_MIXING * (current_n - new_n);
        }
        current_n = new_n;

        // Get the delta
        double sum = 0;
        for (int i = 0; i < GRIDSIZE; i++) {
            double k = grid(i);
            double e_k_i = e_k(delta, grid(i), mu);
            double partial = 0.5 * G_COEFF;
            partial *= u_k_v_k(k, e_k_i, mu);
            partial *= (fermi_dirac(-e_k_i, mu, t) - fermi_dirac(e_k_i, mu, t));
            sum += partial;
        }
        delta = sum;

        // Exit condition
        if (fabs(delta_prev - delta) < DELTA_PRECISION) {
            conv_outcome out = { delta, current_n, iterations };
            return out;
        }

        if (iterations > ALPHA_MIXING_THRESHOLD) {
            delta_prev = ALPHA_MIXING * delta + (1 - ALPHA_MIXING) * delta_prev;
        } else {
            delta_prev = delta;
        }
        iterations++;
    }
}
