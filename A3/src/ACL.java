import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ACL {

    public static boolean matchIP (String[]mask, String[]src, String[]test){
        boolean match = false;
        for (int k = 0; k < test.length; k++){
            if (mask[k].equals("0")){
                if(src[k].equals(test[k])){
                    match = true;
                } else {
                    match = false;
                    break;
                }
            }
        }
        return match;
    }

    public static void extendedACL (ArrayList<String>instruction, ArrayList<String>test){
        Boolean[] result = new Boolean[test.size()];
        Arrays.fill(result, false);

        ArrayList<String> test_original = (ArrayList<String>)test.clone();

        for (int i = 0; i < instruction.size()-2; i++){
            String [] ins = instruction.get(i).split(" ");
            String [] src = ins[4].split("\\.");
            String [] src_mask = ins[5].split("\\.");
            String [] dest = ins[6].split("\\.");
            String [] dest_mask = ins[7].split("\\.");

            for (int j = 0; j < test.size(); j++){
                if(!result[j]) {
                    String [] t = test.get(j).split(" ");
                    String[] src_t = t[0].split("\\.");
                    String[] dest_t = t[1].split("\\.");

                    /*
                     * Assumed that every IP does not have explicit port number,
                     * so set port to 22.
                     */
                    boolean port_match;
                    if(ins[3].equals("ip")){
                        port_match = true;
                    } else {
                        port_match = ins[9].equals(t[2]);
                        if(ins[9].length() > 3){
                            String [] range_ports = ins[9].split("-");
                            for (String rangePort : range_ports) {
                                if (rangePort.equals(t[2])) {
                                    port_match = true;
                                    break;
                                }
                            }
                        }
                    }

                    boolean src_match = matchIP(src_mask, src, src_t);
                    boolean dest_match = matchIP(dest_mask, dest, dest_t);

                    /*
                     * Deal with range ports, assume ip doesn't have port mentioned and every other request
                     * mentions explicit port. Every port length greater than three is a range of port.
                     */

                    if(src_match && dest_match && port_match){
                        if(ins[2].equals("permit")){
                            result[j] = true;
                        }  else {
                            test.set(j, "0.0.0.0 0.0.0.0 0.0");
                        }
                    }
                }
            }
        }


        String str = "denied";
        for(int k = 0; k < test_original.size(); k++) {
            String [] input = test_original.get(k).split(" ");
            if (result[k])
                str = "permitted";
            System.out.println("Packet from " + input[0] + " to " + input[1] + " on port " + input[2] + " " + str);
            str = "denied";
        }
    }
    public static void standardACL(ArrayList<String>instruction, ArrayList<String>test){
        ArrayList<String> test_original = (ArrayList<String>)test.clone();

        Boolean[] result = new Boolean[test.size()];
        Arrays.fill(result, false);

        for (int i = 0; i < instruction.size()-2; i++){
            String [] ins = instruction.get(i).split(" ");
            String [] mask = ins[4].split("\\.");
            String [] ip = ins[3].split("\\.");

            for (int j = 0; j < test.size(); j++){
                if(!result[j]) {
                    String[] t = test.get(j).split("\\.");

                    boolean match = matchIP(mask, ip, t);

                    if(match){
                        if(ins[2].equals("permit")){
                            result[j] = true;
                        } else {
                            test.set(j, "0.0.0.0");
                        }
                    }
                }
            }
        }

        String str = "denied";
        for(int l = 0; l < result.length; l++){
            if (result[l])
                str="permitted";
            System.out.println("Packet from " + test_original.get(l) + " " + str);
            str="denied";
        }
    }

    public static void main(String[] args) {

        // reading the text file with ACL statements
        ArrayList<String> instruction = new ArrayList<>();
        try {
            /* Read the file with ACL instructions here */
            File myObj = new File("C:\\Users\\Tasne\\OneDrive - Dalhousie University\\Desktop\\4174\\" +
                    "Assignment 03\\A3\\A3\\src\\ex-acl-in.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                instruction.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

        // reading the text files with test addresses
        ArrayList<String> test = new ArrayList<>();
        try {
            /* Read the file with IP addresses here */
            File myObj = new File("C:\\Users\\Tasne\\OneDrive - Dalhousie University\\Desktop\\4174\\" +
                    "Assignment 03\\A3\\A3\\src\\ex-acl-test.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                String data = myReader.nextLine();
                test.add(data);
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }

//        System.out.println(instruction); // print the ACL statements
//        System.out.println(test); // print the IP address to permit/deny

        /* Choose which method to use based on the length of the instruction */
        if (instruction.get(0).length() > 39)
            extendedACL(instruction, test);
        else
            standardACL(instruction, test);
    }
}