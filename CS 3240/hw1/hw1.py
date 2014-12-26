# CS 3240 Homework 1
# August 28, 2014
# pnl8zp

__author__ = 'Philip Liberato'
__email__ = 'pnl8zp@virginia.edu'

from math import *


class PointObject(object):
    def __init__(self, category, x_val, y_val):
        self.category = category
        self.x_value = float(x_val)
        self.y_value = float(y_val)
        self.distance = 100

    def __gt__(self, obj2):
        return self.distance > obj2.distance

    def modify_distance(self, x_val, y_val):
        new_distance = sqrt(pow(abs(self.x_value - float(x_val)), 2) + pow(abs(self.y_value - float(y_val)), 2))  # calculate the new euclidean distance
        self.distance = new_distance

    def __str__(self):
        x = self.x_value
        y = self.y_value
        if x.is_integer():
            x = int(x)
        if y.is_integer():
            y = int(y)
        return str(self.category) + " " + str(x) + " " + str(y) + " " + "%.3f" % self.distance

k_value = int(raw_input("Enter k: "))
M_value = int(raw_input("Enter M: "))
file_name = raw_input("Enter the file name: ")

if k_value > M_value:
    k_value = M_value

unclassified_list = []
unclassified = " "

while unclassified != "1.0 1.0" and unclassified != "1 1":
    unclassified = raw_input("Enter an unclassified data value: ")
    point = []
    if unclassified != "1.0 1.0" and unclassified != "1 1":
        for val in unclassified.split():
            point.append(float(val))
        unclassified_list.append(point)


category_points = []
the_file = file(file_name, 'r')
for x in range(0, M_value):
    try:
        parsed_string = the_file.readline().split()
        obj = PointObject(parsed_string[0], parsed_string[1], parsed_string[2])
        category_points.append(obj)
    except IndexError:
        break

the_file.close()

for unclassified_point in unclassified_list:
    for point in category_points:
        point.modify_distance(unclassified_point[0], unclassified_point[1])

    category_points.sort()
    category_counts = [0, 0]
    category_1 = "1"
    category_2 = "2"
    category_1_avg = [0.0, 0.0]
    category_2_avg = [0.0, 0.0]
    for x in range(0, k_value):
        if x is 0:
            category_1 = category_points[0].category
            category_counts[0] += 1
            category_1_avg[0] += category_points[0].x_value
            category_1_avg[1] += category_points[0].y_value
            print category_points[x]
        elif category_points[x].category == category_1:
            category_counts[0] += 1
            category_1_avg[0] += category_points[x].x_value
            category_1_avg[1] += category_points[x].y_value
            print category_points[x]
        else:
            category_2 = category_points[x].category
            category_counts[1] += 1
            category_2_avg[0] += category_points[x].x_value
            category_2_avg[1] += category_points[x].y_value
            print category_points[x]

    if category_counts[0] > category_counts[1]:
        print "Data item (" + str(unclassified_point[0]) + "," + str(unclassified_point[1]) + ") assigned to: " + category_1
    else:
        print "Data item (" + str(unclassified_point[0]) + "," + str(unclassified_point[1]) + ") assigned to: " + category_2

    if category_counts[0] == 0:
        print "Average Distance to " + category_1 + " could not be determined."
    else:
        category_1_avg[0] /= category_counts[0]
        category_1_avg[1] /= category_counts[0]
        val = sqrt(pow(abs(unclassified_point[0] - category_1_avg[0]), 2) + pow(abs(unclassified_point[1] - category_1_avg[1]), 2))
        print "Average distance to " + category_1 + " items: " + str(val)
    if category_counts[1] == 0:
        print "Average Distance to " + category_2 + " could not be determined."
    else:
        category_2_avg[0] /= category_counts[1]
        category_2_avg[1] /= category_counts[1]
        val = sqrt(pow(abs(unclassified_point[0] - category_2_avg[0]), 2) + pow(abs(unclassified_point[1] - category_2_avg[1]), 2))
        print "Average distance to " + category_2 + " items: " + str(val)
    print " "