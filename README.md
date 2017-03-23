## Welcome to Foundify 

Foundify is a chat bot for those interested in founding their own company. It works like a finite state machine with two classes at its heart: `DialogState` and `DialogStateController`.

 For language processing we use **GermaNet** and semantic relatedness measures between two words (so called synsets) as well as **IBM Watson Language Classifier** to archieve a reasonable understanding of human language.


----------


### Features

 - Finding appropriate assistance programs for startups in a natural dialog driven environment
 - Reusable and easily extendable with new features 


----------


### Project structure

 - **[src](https://github.com/SinjeK/foundify/tree/master/src "src")**
	- [hu:](https://github.com/SinjeK/foundify/tree/master/src/hu) *This folder contains the actual code*
		- [berlin/dialog](https://github.com/SinjeK/foundify/tree/master/src/hu/berlin/dialog): *Main code of chat bot*
		- [berlin/file:](https://github.com/SinjeK/foundify/tree/master/src/hu/berlin/file) *Helper class to access files*
	- [gn:](https://github.com/SinjeK/foundify/tree/master/src/gn) *This folder contains all GermaNet related data*
		- [ressources/frequencies:](https://github.com/SinjeK/foundify/tree/master/src/gn/ressources/frequencies) *Data containing the occurrence of words (synsets) in german language*
		- [ressources/v90XML:](https://github.com/SinjeK/foundify/tree/master/src/gn/ressources/v90XML) *the actual GermaNet data*
	- [json:](https://github.com/SinjeK/foundify/tree/master/src/json) *This folder contains a library for parsing JSON files* 
	
	- `GermaNetSemRelApi9.1.jar`: *This jar contains the GermaNet API as well as the semantic relatedness API* (currently not used)

	- `IBM-WatsonApi3.5.3.jar`: *This jar contains the IBM Cloud API used to access the Watson Natural Language Classifier*
	
	- `Main.java`: *This is the starting routine of the chat bot*

 - `nullrelatedness.ini`: *This configuration file is needed for the semantic relatedness API. If it is not found, the ini file will be created which can take up several minutes.*  

----------

### External libraries

 - [IBM Watson Developer Cloud API](http://watson-developer-cloud.github.io/java-sdk/docs/master/)
 - [Stanford CoreNLP](http://nlp.stanford.edu/software/)


----------


### Guide
Follow these instructions to setup, execute, modify or extend the project.

> **Be careful!**
> Being under active development this project is very likely to change. Concepts and codes described in this document might be outdated. Refer to the documentation in the corresponding classes for full reference.

#### Setup Git

 1. `git clone git@github.com:SinjeK/foundify.git`
 2. You are ready to go!

> **Note:**
> A good short git guide can be found [here](https://rogerdudler.github.io/git-guide/index.de.html). For a comprehensive guide take a look [here](https://www.atlassian.com/git/tutorials/learn-git-with-bitbucket-cloud).

#### Setup project

 1. Download the project (see Setup Git).
 2. Add `IBM-Watson.jar` to your class path which can be found under the
    src directory.
 3. Now you also need to download **StanfordCoreNLP with the German models** which can be found here: http://stanfordnlp.github.io/CoreNLP/.
    Add these files to the class path.
 4. Thats it!

#### Additional flags
You might need to add these following flags when executing this project.

    -Xmx512m -Xms512m

This **increases the heap size of java virtual machine** to 512 MB. Change the size to your needs.

> **Note:**
> If you want to assign 1 GB to the heap, you can add `-Xmx1g -Xms1g` or `-Xmx1000m -Xms1000m`. `-Xmx` sets the final heap size and `-Xms`sets the initial heap size.

So if you run this chat bot:

    java -Xmx512m -Xms512 project-name

#### Enable assertions
This project include several assertions which makes the hard life of debugging easier. Add the flag below:

    -ea

----------

### Production code
If you are only interested in running the chatbot, take a look at the [Google Drive folder](https://drive.google.com/drive/folders/0B6HxYLUk0dp7M1Q3VnVhcVBkLWM?usp=sharing).

 1. Download the folder
 2. Create a new project with your favourite JAVA IDLE/ Editor
 3. Add all downloaded files to the new project
 3. Add all three Stanford jar files to the class path found in the src folder
 4. Add IBM Watson.jar file to the class path found in the src folder
 5. Compile the project
 6. Add these java vm flags `-Xmx1g -Xms1g`  when executing this project (otherwise you might run out of memory)
 7. Run foundify!


----------


#### `DialogState`

A `DialogState` represents a state which the chat bot can reach. Every state should be designed in a way that it is responsible **for only one task**.

 For instance, this chat bot has the states `Welcome`, which is only responsible for greeting the user, and `AssistancePrograms`, whose task is to find suitable assistance programs based on the user's input.

##### Create a DialogState
To create a new dialog state you must subclass the abstract class `DialogState`. Therefore the following methods must be overridden:

 - `abstract void enter()`: *This is the **starting point of your state**. Implement what the state should do first. For example, ask for the user's name, the birthday, ...*
 
 - `abstract void evaluate(String input)`: *Implement this to **process the user's input**. For example, if you have asked for the user's age and the String input contains a numerical value, save this in a database as user's age*

If you want to signal that your state has finished doing its job, call `leave()`. If you want to print a message to the user, use `put("Hey, I am Foundify")`.

This is one example dialog state:

    package hu.berlin.dialog;
    
    public class Age extends DialogState {
		 @Overridden
		 public void enter() {
			 put("How old are you?");
		 }

		@Overridden
		public void evaluate(String input) {
			int age = Integer.parseInt(input);
			saveAgeToDatabase(age);

			// the state has finished doing its job
			leave();
		}
	}

>**Subclassing Note**
> The method `void put(String input)` should not be overridden. If you do, **do not forget to call** `super()`.
>
>If you override `void leave()` **make sure to call** `super()`in the implementation although the current version does nothing but this might change in future.
 
 
#### `DialogStateController`

A DialogStateController is responsible for handling the output of the states and for forwarding the user's input to the correct state. Additionally, it determines the correct states after a state has finished doing its job.

    package hu.berlin.dialog;
    
    public class Controller implements DialogStateController {

		// implement setter and getter
		private State currentState;

		 @Overridden
		 public void dialogStateWantsToOutput(DialogState state, String output) {
			 System.out.println(output);
		 }

		@Overridden
		public void dialogStateDidLeave(DialogState state) {
			switch(state.getIdentifier()) {
				case "NAME": {
					// state "NAME" has finished getting the user's name
					// transition to next state "AGE"
					DialogState Age = new AgeState();
					Age.enter();
					this.setCurrentState(Age);
				}
			}
		}
	}
 
