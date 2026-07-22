/**
 * Launches Encounter Builder app 
 */
public class Main {
    /**
     * Entry point for Encounter program
     */
        void main() {
        new EncounterGUI();
    }
}  
        
        /*
            Create the Encounter.... (OLD HARD CODED TEST ENCOUNTER)
            OUTSIDE OF CLASS AND METHOD DO NOT UNCOMMENT
        */
        // Scanner input = new Scanner(System.in);
        
        // boolean running = true;

        /*
            Menu Choices... First display encounter then print menu choices (OLD TERMINAL BASED VERSION)
         */
//         while(running){
//             IO.println(e.toDisplay()); 
//             IO.println("\n=== MENU ===");
//             IO.println("1. Next Turn");
//             IO.println("2. Damage");
//             IO.println("3. Heal");
//             IO.println("4. Remove Combatant");
//             IO.println("5. Add condition");
//             IO.println("6. Remove condition");
//             IO.println("7. Exit");
            
//             IO.print(Color.CYAN + "\nChoice: " + Color.RESET);
//             int choice = input.nextInt();
//             input.nextLine();

//             /*
//                 Choice logic .... Next turn, Deal damage, Heal, Remove, and Exit
//              */
//             switch(choice){
//                 case 1: //Next turn
//                     e.nextTurn();
//                     break;
//                 case 2: //Damage
//                     IO.println(Color.RED + "\nTarget Name: ");
//                     String damageTarget = input.nextLine();
//                     Combatant damaged = e.findCombatant(damageTarget);
//                     if(damaged == null){//Force another choice if an invalid comabtant name is inputed 
//                         while(damaged == null){
//                             IO.println("Combatant not found.");;
//                             IO.println(Color.RED + "\nPick Another Target Name: ");
//                             damageTarget = input.nextLine();
//                             damaged = e.findCombatant(damageTarget);
//                         }
//                     }
//                     IO.println("How much damage?");
//                     int damageAmt = input.nextInt();
//                     input.nextLine();
//                     damaged.takeDamage(damageAmt);
//                     IO.println(damaged.getName() + " took " + damageAmt + " damage, they now have " + damaged.getCurHP() + " HP." + Color.RESET);
                     
//                     break;
//                 case 3: //Heal
//                     IO.println(Color.GREEN + "\nHeal Target Name: ");
//                     String healTarget = input.nextLine();
//                     Combatant healed = e.findCombatant(healTarget);
//                     if(healed == null){//Force another choice if an invalid comabtant name is inputed 
//                         while(healed == null){
//                             IO.println("Combatant not found.");;
//                             IO.println("\nPick Another Target Name: ");
//                             healTarget = input.nextLine();
//                             healed = e.findCombatant(healTarget);
//                         }
//                     }
//                     IO.println("How much healing?");
//                     int healAmt = input.nextInt();
//                     input.nextLine();
//                     healed.heal(healAmt);
//                     IO.println(healed.getName() + " recieved " + healAmt + " healing, they now have " + healed.getCurHP() + " HP." + Color.RESET);
                    
//                     break;

//                 case 4: 
//                     IO.println("Remove who?");
//                     String removeName = input.nextLine();
//                     Combatant removed = e.findCombatant(removeName);
//                     if(removed == null){//Force another choice if an invalid comabtant name is inputed 
//                         while(removed == null){
//                             IO.println("Combatant not found.");;
//                             IO.println("\nPick Another Remove Target: ");
//                             removeName = input.nextLine();
//                             removed = e.findCombatant(removeName);
//                         }
//                     }
//                     e.removeCombatant(removeName);
//                     break;

//                 case 5:
//                     IO.println(Color.YELLOW + "condition Target Name: ");
//                     String targetName = input.nextLine();
//                     Combatant target = e.findCombatant(targetName);
//                     if(target == null){
//                         while(target == null){
//                             IO.println("Combatant not found.");;
//                             IO.println("\nPick Another Target: ");
//                             targetName = input.nextLine();
//                             target = e.findCombatant(targetName);
//                         }
//                     }
//                     IO.println("condition Name: ");
//                     String conditionName = input.nextLine();
//                     IO.println("Duration: ");
//                     int duration = input.nextInt();
//                     input.nextLine();
//                     target.addConditions(new Condition(conditionName, duration));
//                     IO.println(conditionName + " added to " + targetName + Color.RESET);
                    
//                     break;
                
//                 case 6: 
//                     IO.println(Color.YELLOW + "Remove condition from who?");
//                     targetName = input.nextLine();
//                     target = e.findCombatant(targetName);
//                     if(target == null){
//                         while(target == null){
//                             IO.println("Combatant not found.");;
//                             IO.println("\nPick Another Target: ");
//                             targetName = input.nextLine();
//                             target = e.findCombatant(targetName);
//                         }
//                     }
//                     IO.println("condition Name: ");
//                     conditionName = input.nextLine();

//                     target.removeConditions(conditionName);
//                     IO.println(conditionName + " removed from " + targetName + Color.RESET);
                    
//                     break;

//                 case 7: 
//                     running = false;
//                     IO.println(Color.BG_RED + "Encounter Ended." + Color.RESET);
//                     break;
//                 default:
//                     String invalid = Color.BG_YELLOW + "Invalid menu choice." + Color.RESET;
//                     IO.println(invalid);
//             }
//         }
//         input.close();

