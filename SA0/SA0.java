/**
 * SA0.java — a fun lil group of methods & properties to model a "philosopher" w/ Java 
 * (philosopher is very broadly defined...also don't take this too seriously)
 * Stereotypes are based on this research: 
 * https://onlinelibrary.wiley.com/doi/full/10.1002/ejsp.842 
 * https://philpapers.org/surveys/
 * 
 * @author Jeremy Hadfield
 * @date Mar 25, 2019
 * @category Dartmouth CS10 19S, Short Assignment 0 
 * 
 */
public class SA0 {
	private String name; 
	private int age; 
	private String gender; 
	private int birthYear;
	private int rating; 
	private int hardness;
	private String country;
	private String fieldOfStudy;
	private String[] books;

	/**
	 * Constructor with multiple arguments 
	 * 
	 * @param name — Full name of the philosopher 
	 * @param age — Current age of philosopher if still living; age of death if 
	 * @param birthYear — the year the philosopher was born
	 * @param gender — gender the philosopher identified with 
	 * @param rating — how good the philosopher is at being good (e.g. Plato = 9, Confucius = 8, Jesus = 10, Satan = 0) 
	 * @param hardness — considers how hard the philosopher's life was & how sad they were 
	 * @param country — philosopher's country of residence
	 * @param fieldOfStudy — philosopher's primary field of study (e.g. epistemology, ethics, logic, etc) 
	 * @param books — top 5 works published by this philosopher 
	 */
	public SA0(String name, int age, String gender, int birthYear, int rating, int hardness, String country,
			String fieldOfStudy, String[] books) {
		this.setName(name);
		this.setAge(age); 
		this.setGender(gender);
		this.setBirthYear(birthYear); 
		this.setRating(rating);
		this.setHardness(hardness); 
		this.setCountry(country);
		this.setFieldOfStudy(fieldOfStudy);
		this.setBooks(books);
	}


	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @param age the age to set
	 */
	public void setAge(int age) {
		this.age = age;
	}
	
	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender the gender to set
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return the birthYear
	 */
	public int getBirthYear() {
		return birthYear;
	}

	/**
	 * @param birthYear the birthYear to set
	 */
	public void setBirthYear(int birthYear) {
		this.birthYear = birthYear;
	}

	/**
	 * @return the rating
	 */
	public int getRating() {
		return rating;
	}

	/**
	 * @param rating the rating to set
	 */
	public void setRating(int rating) {
		this.rating = rating;
	}

	/**
	 * @return the hardness
	 */
	public int getHardness() {
		return hardness;
	}

	/**
	 * @param hardness the hardness to set
	 */
	public void setHardness(int hardness) {
		this.hardness = hardness;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the fieldOfStudy
	 */
	public String getFieldOfStudy() {
		return fieldOfStudy;
	}

	/**
	 * @param fieldOfStudy the fieldOfStudy to set
	 */
	public void setFieldOfStudy(String fieldOfStudy) {
		this.fieldOfStudy = fieldOfStudy;
	}

	/**
	 * @return the books
	 */
	public String[] getBooks() {
		return books;
	}

	/**
	 * @param books the books to set
	 */
	public void setBooks(String[] books) {
		this.books = books;
	}
	
	/**
	 * Gets a summary of this philosopher's works
	 */
	public String getSummary() {
		return country; 
	}
	
	/**
	 * Spits out a string of lies
	 */
	public String makeUpMetaphysics() {
		return "Yes I know what I'm talking about and I understand existence more than everyone else. As "
				+ "you can see I wrote all these books: " + books + ". I am truly the wokest being alive.";
	}
	
	/** 
	 * Returns a response where the philosopher defends itself from criticism
	 */
	public String defendSelf() { 
		return "What the f*** did you just say about me, you unenlightened fool?" + 
				"I'll have you know I graduated with a PhD at a university with a pretentious-sounding name like 'Oxford.'" +
				"So yes, you ignoramus, I am an actual doctor of PHILOSOPHY." +
				"I've been involved in numerous internet debates with irrational haters," + 
				"and I have over 300 confirmed deconstructions." +
				"I am trained in the postmodern Western philosophical tradition and I'm the top scholar in the entire " +
				fieldOfStudy + "movement. You are nothing to me but just another strawman. I will wipe you the f*** out with " + 
				"logic the likes of which has never been seen before on this Earth, mark my f****** words." + 
				"As we speak I am contacting my secret network of metaphysicists across the USA and your" +
				"arguments are being analysed right now so you better prepare for the storm, maggot. The storm that wipes" + 
				"out the pathetic little thing you call your intellectual credibility. You're f****** wrong, hater." +
				"I can be anywhere, anytime, and I can destroy your arguments in over seven hundred ways, and that's" +
				"just with my copy of Nichomachean Ethics. Not only am I extensively trained in Kantian ethics, but I have access" +
				"to the entire arsenal of the " + fieldOfStudy + "juggernaut and I will use it to its full extent to wipe your" +
				"miserable *** off the face of the humanities, you little ****. If only you could have known what unholy" + 
				"retribution your little \"clever\" comment was about to bring down upon you, maybe you would have held your" + 
				"f****** tongue. But you couldn't, you didn't, and now you're paying the price, you god**** idiot. I will spray" + 
				"fury all over you and you will drown in it. I foresee a lot of running for the hills, kiddo.";
	}
	
	/**
	 * Creates a few example philosopher objects
	 * @param args
	 */
	public static void main(String[] args) {
		String[] stereotypeBooks = {"Critique of the Critique of Pure Reason", "Token Work of Fiction to Show I Can Write", 
				"Deconstructing Everything I Find", "The Metaphysics of Dasein or Some Other Word I Made Up", 
				"I Understand Existence More Than All Y'all Fools"};
		SA0 stereotype = new SA0("Immanuel Locke", 79, "Male", 1724, 5, 5, "Germany", "Metaphysics", stereotypeBooks);
		System.out.println(stereotype.defendSelf());
		System.out.println(stereotype.getSummary()); 
		
		String[] nietzscheBooks = {"Thus Spake Zarathustra", "Beyond Good and Evil", "The Antichrist", 
				"Human, All Too Human", "The Gay Science"};
		SA0 nietzsche = new SA0("Friedrich Nietzsche", 55, "Male", 1844, 4, 8, "Germany", "Metaphysics", nietzscheBooks);
		System.out.println(nietzsche.defendSelf());
		System.out.println(nietzsche.getSummary());  
	}
}
