# Turing Machine Simulator

Input a valid encoding of a Turing Machine adhering to the following encoding format (instructions also listed in application):
1. Number of States
2. Number of Rules in Turing Machine
3. Rules: (State    Character on Tape    Next State    Write Character    Move Direction(L or R))
4. Accept State
5. Input String

## Example
Encoding to calculate **n mod 2** of a number (B represents blank/empty tape cell)
 ```sh
5
8
0 B 2 B L
0 1 1 1 R
1 1 0 1 R
1 B 3 B L
2 1 2 B L
2 B 4 B R
3 1 3 B L
3 B 4 1 R
4
B111B
 ```

### Result
Shows if Turing Machine accepts or fails for given input string and allows for user to step through the steps of simulation one-by-one, showing the result of each step on the tape below

<img width="521" alt="turingmachine" src="https://github.com/zdpel/TuringMachineSim/assets/159809176/ca3bed54-2408-4058-b8e3-3ed9ad6d5564">
