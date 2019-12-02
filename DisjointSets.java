// created by Harry Specter

import java.util.ArrayList;
import java.util.Scanner;


class Node {
    int data;
    Node next;
    Representative head;

    Node(int data) {
        head = null;
        next = null;
        this.data = data;
    }
}

class Representative {
    Node tail;
    Node head;
    int weight;

    Representative() {
        head = null;
        tail = null;
        weight = 0;
    }
}

public class Sets {

    static ArrayList<Representative> sets;

    //makeset to add elements to a set
    static Representative makeset(int setNo, int value) {
        // node creation
        Node newNode = new Node(value);
        // obtain representative of the set for which the member has to inserted
        Representative r1 = sets.get(setNo - 1);
      	// disjoint sets cannot have repetition of elements
        if (findset(value) > -1) {
            System.out.println("Element already exists in set!");
            return r1;
        }
        else
        	System.out.println("Inserting element in set...");
        newNode.head = r1;
        // linked list insertion
        if (r1.head == null) {
            r1.head = newNode;
            r1.tail = newNode;
            r1.weight += 1;
            return r1;
        }
        Node node = r1.head;
        while (true) {
            if (node.next != null)
                node = node.next;
            else {
                node.next = newNode;
                r1.tail = newNode;
                r1.weight += 1;
                break;
            }
        }
        return r1;
    }

    static Representative deleteElement(int setNo, int value) {
        // obtain corresponding set representative
        Representative delRep = sets.get(setNo - 1);
        Node delNode = delRep.head;
        Node prevNode = delNode;
        // loop till the element to delete is found
        while (delNode.data != value && delNode != null) {
            prevNode = delNode;
            delNode = delNode.next;
        }
        // element found
        // has four cases
        if (delNode != null) {
            // case 1 - only node
            if (delRep.head == delNode && delRep.tail == delNode) {
                delRep.head = null;
                delRep.tail = null;
            }
            // case 2 - head
            else if (delRep.head == delNode) {
                delRep.head = delNode.next;
            }
            // case 3 - tail
            else if (delNode.next == null) {
                prevNode.next = null;
                delRep.tail = prevNode;
            }
            // case 4 - middle element
            else
                prevNode.next = delNode.next;
            delRep.weight -= 1;
        }
        // element not found 
        else
            System.out.println("Element not found in set!");
        return delRep;
    }

    static void printSets(int number, Representative rep) {
        System.out.print("Set " + number + " : { ");
        Node node = rep.head;
        // when set is empty print empty
        if (node == null) {
            System.out.print("Empty }\n");
            return;
        }
        // else loop through nodes of the set
        while (node != null) {
            System.out.print(node.data + " ");
            node = node.next;
        }
        System.out.print("}\n");
    }

    static int findset(int value) {
        int found = 0;
        // loop through every set
        // return result's rep : set number, weight, head and tail
        for (int i = 0; i < sets.size(); i++) {
            Node searchNode = sets.get(i).head;
            if (searchNode != null) {
                while (searchNode.data != value) {
                    if (searchNode.next == null)
                        break;
                    searchNode = searchNode.next;
                }
                if (searchNode.data == value) {
                    // return data
                    found = 1;
                    return i;
                }
            }
        }
        if (found == 0)
            System.out.println("Element not found! ");
        return -1; // set number returned for element not found case 
    }

    static Representative union(Representative r1, Representative r2) {
    	// to append smaller set to the larger one
        // Make r1 as the set with the larger weight value 
        // and r2 the set with smaller value of weight    	
        if (r2.weight > r1.weight) {
            Representative temp = r1;
            r1 = r2;
            r2 = temp;
        }
        //appending
        r1.tail.next = r2.head;
        r1.tail = r2.tail;
        Node node = r2.head;
        // updating representative of appended nodes and the weight of the set
        while (node != null) {
            node.head = r1;
            node = node.next;
            r1.weight +=1;
        }
        return r1;
    }

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        sets = new ArrayList<Representative>();   // list of representatives for the sets
        int count = 0;  // number of sets created
        // variables for processing
        int value;
        int setNo, choice;
        while (true) {
            System.out.println("Menu :");
            System.out.println(
                    "1. Create set\n2. Delete set\n3. Add element to set\n4. Remove element from set\n5. Find set\n6. Union\n7.Exit");
            choice = input.nextInt();
            switch (choice) {
            case 1:
                // create a new representative element
                Representative rep = new Representative();
                sets.add(rep);
                count++;
                System.out.println("Set " + count + " Created !");
                break;
            case 2:
                // delete a representative element
                System.out.println("Enter set number to remove :");
                setNo = input.nextInt();
                if (setNo > count) {
                    System.out.print("Set " + setNo + " doesn't exist . ");
                    break;
                }
                sets.remove(setNo - 1);
                count--;
                break;
            case 3:
                // makeset
                System.out.println("Enter set number to add element :");
                setNo = input.nextInt();
                if (setNo > count) {
                    System.out.println("Set " + setNo + " doesn't exist . ");
                    break;
                }
                System.out.println("Enter element data :");
                value = input.nextInt();
                Representative r = sets.get(setNo - 1);
                r = makeset(setNo, value);
                printSets(setNo, r);
                break;
            case 4:
                // remove element
                System.out.println("Enter element data :");
                value = input.nextInt();
                setNo = findset(value);
                if (setNo < 0)
                    System.out.println("Element doesn't exist!");
                else {
                    setNo++;
                    Representative rDel = deleteElement(setNo, value);
                    printSets(setNo, rDel);
                }
                break;
            case 5:
                // findset - returns representative element of a given element
                System.out.println("Enter element data :");
                value = input.nextInt();
                int i = findset(value);
                if (i >= 0) {
                    Representative searchRep = sets.get(i);
                    System.out.println("Element found!");
                    System.out.println("Set No. : " + (i + 1) + " Weight : " + searchRep.weight + " Head : "
                            + searchRep.head.data + " Tail : " + searchRep.tail.data);
                }
                break;
            case 6:
                // Union of two sets - return representative element of first set
                System.out.println("Enter element x :");
                value = input.nextInt();
                System.out.println("Enter element y :");
                int value2 = input.nextInt();
                int m = findset(value);
                int n = findset(value2);
                if (m < 0 || n < 0)
                    System.out.println("Cannot perform union! Element not found in sets!");
                else if(m==n){
                    System.out.println("Elements already present in the same set!");
                }
                else {
                	System.out.println("Performing union...");
                    Representative unionRep = sets.get(m);
                    unionRep = union(sets.get(m), sets.get(n)); // union func parameterised with the reps of the sets
                    // after appending one set to another, delete the redundant remaining set
                    if(sets.get(m).weight<sets.get(n).weight)
                        sets.remove(m);
                    else
                        sets.remove(n); 
                    count--;
                    printSets(m + 1, unionRep);
                }
                break;
            case 7:
                input.close();
                System.out.println("Exit !");
                break;
            default:
                System.out.println("Invalid choice !");
            }
            if (choice == 7)
                break;
        }

    }
}
