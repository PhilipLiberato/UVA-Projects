import sys
import os
import locale

if len(sys.argv) != 2:
    print("\n> Improper Syntax For Command Line Parameters!")
    print("> Expected Input: 'python hw1.py nameOfTheTestFile.txt'")
    sys.exit()

if not os.path.isfile(sys.argv[1]):
    print("> The given file does not exist!")
    sys.exit()

theFile = open(sys.argv[1], 'r')
locale.setlocale(locale.LC_ALL, '')

change = [" Q", " D", " N", " P"];
value = [25, 10, 5, 1];
print()

for line in theFile:
    if line == "-1.00":
        theFile.close()
        sys.exit()
    else:
        result = "$" + "{:,.2f}".format(float(line));
        amount = int(float(line.split(".")[1]));
        if not amount == 0:
            for num in range(0, 4):
                if amount == 0: break;
                for iter in range (0, int(amount / value[num])):
                    result += change[num];
                amount %= value[num];
        print(result)
