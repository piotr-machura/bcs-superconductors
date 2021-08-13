#include<stdio.h>
#include<stdint.h>
#include <stdlib.h>
#include"conf.h"
#include"calc.h"


int main(void) {
    double mu = MU_INITIAL, t = T_INITIAL;
    FILE *output_file = fopen(OUTPUT_FILE, "w");

    printf("\033[1mCalculating... ");
    fflush(stdout);
    for (uint64_t i = 0; i < T_MAX_STEPS; i++) {
        t *= T_MULTIPLIER;
        conv_outcome out = converge_delta(mu, t);
        fprintf(output_file, "%f %f %f %ld\n", t, out.delta, out.n_particles, out.iterations);
        if (out.delta <= DELTA_ZERO) {
            printf("\033[32;1m done \033[0m\n");
            break;
        }
    }
    fclose(output_file);
    return 0;
}
