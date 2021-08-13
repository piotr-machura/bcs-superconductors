# %% [markdown]
# # BCS equations
# This notebook analyzes data obtained by numerically solving the BCS equations
# of a homogenous system. The data is obtained by compiling the code with GNU
# make and running the program.
#
# Configuration options are available in the `include/conf.h` file. Adjust them
# as necessary before compiling.

# %%
import os
import pandas as pd
from matplotlib import pyplot as plt
from math import pi
import matplotlib as mpl

mpl.rcParams['figure.dpi'] = 100
os.chdir(os.path.abspath(''))

df = pd.read_csv(
    'res.txt',
    delim_whitespace=True,
    names=['Temp', 'Delta', 'N', 'Iter'],
).sort_values(by='Temp')

# %%
plt.grid()
plt.title('Delta vs temperature')
plt.plot(df['Temp'], df['Delta'])
ratio = df.Delta.iat[0] / df.Temp.iat[-1]
print(f'Ratio: {ratio}')

# %%
fermiE = [0.5 * (3 * pi * pi * n / (100 * 1e-3))**(2 / 3) for n in df.Iter]
plt.grid()
plt.title('Fermi energy vs temperature')
plt.plot(df['Temp'], fermiE)
for e in fermiE:
    if e > 1:
        print('Could not represent physical system')
        break
else:
    print('Could represent physical system')
