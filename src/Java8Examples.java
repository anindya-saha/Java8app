import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
// https://www.mkyong.com/java8/page/2/
// http://www.oracle.com/technetwork/articles/java/ma14-java-se-8-streams-2177646.html
// http://www.oracle.com/technetwork/articles/java/architect-streams-pt2-2227132.html
public class Java8Examples {
	
	public static void sortMapByKeyByAndValue() {

		Map<String, Integer> unsortedMap = new HashMap<String, Integer>();

		unsortedMap.put("z", 10);
		unsortedMap.put("b", 5);
		unsortedMap.put("a", 6);
		unsortedMap.put("c", 20);
		unsortedMap.put("d", 1);
		unsortedMap.put("e", 7);
		unsortedMap.put("y", 8);
		unsortedMap.put("n", 99);
		unsortedMap.put("j", 50);
		unsortedMap.put("m", 2);
		unsortedMap.put("f", 9);

		System.out.println("Original...");
		System.out.println(unsortedMap);

		// sort a map by key, a,b,c,d,e...
		System.out.println("Sorted By Key...");
		Map<String, Integer> sortedMapByKey = new LinkedHashMap<>();
		unsortedMap.entrySet().stream()
			.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
			.forEachOrdered(entry -> sortedMapByKey.put(entry.getKey(), entry.getValue()));
		System.out.println(sortedMapByKey);
		
		// Alternate way of doing the same thing above
		System.out.println("Sorted By Key : Better Alternate Method...");
		Map<String, Integer> sortedMapByKeyBetter = unsortedMap.entrySet().stream()
														.sorted((a, b) -> a.getKey().compareTo(b.getKey()))
														.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
		System.out.println(sortedMapByKeyBetter);
		
		// sort a map by value, and reverse, 99,50,20,10...
		System.out.println("Sorted By Value...");
		Map<String, Integer> sortedMapByValue = new LinkedHashMap<>();
		unsortedMap.entrySet().stream()
			.sorted((a, b) -> b.getValue().compareTo(a.getValue()))
			.forEachOrdered(entry -> sortedMapByValue.put(entry.getKey(), entry.getValue()));

		System.out.println(sortedMapByValue);
		
		// Alternate way of doing the same thing above
		System.out.println("Sorted By Value : Better Alternate Method...");
		Map<String, Integer> sortedMapByValueBetter = unsortedMap.entrySet().stream()
														.sorted((a, b) -> b.getValue().compareTo(a.getValue()))
														.collect(Collectors.toMap(p -> p.getKey(), p -> p.getValue()));
		System.out.println(sortedMapByValueBetter);
	}

	public static void convertStreamToList() {
		
		// The following will accumulate strings into an ArrayList:
		System.out.println("Convert a Stream of Strings to ArrayList<String>...");
		Stream<String> language = Stream.of("java", "python", "node");
		List<String> languageList = language.collect(Collectors.toList());
		languageList.forEach(System.out::println);
		
		// The following will accumulate numbers into an ArrayList but will also filter out some numbers:
	    System.out.println("Accumulate numbers into an ArrayList but will also filter out some numbers...");
		Stream<Integer> numbers = Stream.of(1, 2, 3, 4, 5);
        List<Integer> numbersList = numbers.filter(x -> x != 3).collect(Collectors.toList());
        numbersList.forEach(x -> System.out.println(x));
        
        // Filter out null values from a Stream
        System.out.println("Accumulate strings into an ArrayList but will also filter out null values...");
        Stream<String> languagesWithNull = Stream.of("java", "python", "node", null, "ruby", null, "php");
        List<String> languagesNulllFiltered = languagesWithNull.filter(x -> x != null).collect(Collectors.toList());
        languagesNulllFiltered.forEach(System.out::println);
	}
	
	public static void groupingByOnStreams() {
		
		class Person {
			
			public String getName() {
				return name;
			}

			public String getState() {
				return state;
			}

			public String getCity() {
				return city;
			}

			private String name, state, city;

			public Person(String name, String state, String city) {
				this.name = name;
				this.state = state;
				this.city = city;
			}		
		}
		
		Supplier<Stream<Person>> personStreamSupplier = () -> Stream.of(new Person[]{new Person("Timothy", "Texas", "Austin"), 
																					 new Person("Adriana", "California", "San Francisco"),
																					 new Person("Lavatno", "California", "Sunnyvale"),
																					 new Person("Andrey", "New York", "Manhattan")});
		
		// The following will classify {Person} objects by city:
		System.out.println("Classify a stream of {Person} objects by city...");
		Map<String, List<Person>> peopleByCity = 
				personStreamSupplier.get().collect(Collectors.groupingBy(Person::getCity));
		System.out.println(peopleByCity);
		
		//The following will classify {Person} objects by state and city, cascading two {Collector}s together:
		System.out.println("Classify {Person} objects by state and city...");
		Map<String, Map<String, List<Person>>> peopleByStateAndCity = 
				personStreamSupplier.get().collect(Collectors.groupingBy(Person::getState, Collectors.groupingBy(Person::getCity)));
		System.out.println(peopleByStateAndCity);
	}
	
	public static void convertMaptToList() {
		
		Map<Integer, String> map = new HashMap<>();
        map.put(10, "apple");
        map.put(20, "orange");
        map.put(30, "banana");
        map.put(40, "watermelon");
        map.put(50, "dragonfruit");

        System.out.println("1. Export Map Key to List...");

        List<Integer> result = map.entrySet().stream()
                .map(x -> x.getKey())
                .collect(Collectors.toList());

        result.forEach(System.out::println);

        System.out.println("2. Export Map Value to List...");

        List<String> result2 = map.entrySet().stream()
                .map(x -> x.getValue())
                .collect(Collectors.toList());

        result2.forEach(System.out::println);
	}
	
	public static void convertListToMap() {
		class Hosting {

		    private int Id;
		    private String name;
		    private Date createdDate;

		    public Hosting(int id, String name, Date createdDate) {
		        Id = id;
		        this.name = name;
		        this.createdDate = createdDate;
		    }

		    //getters and setters
			public int getId() {
				return Id;
			}

			public String getName() {
				return name;
			}

			public Date getCreatedDate() {
				return createdDate;
			}
		}
		
		List<Hosting> list = new ArrayList<Hosting>();
        list.add(new Hosting(1, "liquidweb.com", new Date()));
        list.add(new Hosting(2, "linode.com", new Date()));
        list.add(new Hosting(3, "digitalocean.com", new Date()));

        //example 1
        Map<Integer, String> result1 = list.stream().collect(
                Collectors.toMap(Hosting::getId, Hosting::getName));

        System.out.println("Result 1 : " + result1);

        //example 2
        Map<Integer, String> result2 = list.stream().collect(
                Collectors.toMap(x -> x.getId(), x -> x.getName()));

        System.out.println("Result 2 : " + result2);
	}
	
	public static void streamCannotBeReused() {
		// In Java 8, Stream cannot be reused, once it is consumed or used, the stream will be closed.
		
		// Review the following example, it will throw an IllegalStateException, saying "stream is closed".
		
		String[] array = {"a", "b", "c", "d", "e"};
        Stream<String> stream = Arrays.stream(array);

        // loop a stream
        stream.forEach(System.out::println);

        // reuse it to filter again! throws IllegalStateException
        long count = stream.filter(x -> "b".equals(x)).count();
        System.out.println(count);
	}
	
	public static void resusingAStream() {
		// In Java 8, Stream cannot be reused, once it is consumed or used, the stream will be closed.
		
		// For whatever reason, if we really want to reuse a Stream, try the following Supplier solution
		
		String[] array = {"a", "b", "c", "d", "e"};
		Supplier<Stream<String>> streamSupplier = () -> Stream.of(array);

		//get new stream
        streamSupplier.get().forEach(System.out::println);

        //get another new stream
        long count = streamSupplier.get().filter(x -> "b".equals(x)).count();
        System.out.println(count);
	}
	
	public static void groupingAndSortingOnStream() {
		//3 apple, 2 banana, others 1
        String[] data =new String[] {"apple", "apple", "banana", "apple", "orange", "banana", "papaya"};

        // Group By the count of elements
        System.out.println("Group By the count of elements...");
        Map<String, Long> groupByNameCountMap =
                Stream.of(data).collect(
                        Collectors.groupingBy(
                                Function.identity(), Collectors.counting()
                        )
                );

        System.out.println(groupByNameCountMap);
        
        // Group By the count of elements and then Sort the map based on the count value in descending order
        Map<String, Long> groupByNameAndSortByCountMap = new LinkedHashMap<>();
        System.out.println("Group By the count of elements and then Sort the map based on the count value in descending order...");
        groupByNameCountMap.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .forEachOrdered(entry -> groupByNameAndSortByCountMap.put(entry.getKey(), entry.getValue()));

        System.out.println(groupByNameAndSortByCountMap);
        
        // Group By the count of elements and then Sort the map based on the count value in descending order and then by the name in ascending order
        // using the thenComparing method
        System.out.println("Group By the count of elements and then Sort the map based on the count value in descending order and then by the name in ascending order...");
        Map<String, Long> groupByNameAndSortByCountDescThenByNameAscMap = new LinkedHashMap<>();
        groupByNameCountMap.entrySet().stream()
        .sorted(Map.Entry.<String, Long>comparingByValue()
                .reversed().thenComparing(Map.Entry.<String, Long>comparingByKey()))
        		.forEachOrdered(entry -> groupByNameAndSortByCountDescThenByNameAscMap.put(entry.getKey(), entry.getValue()));

        System.out.println(groupByNameAndSortByCountDescThenByNameAscMap);
	}
	
	public static void groupCountMappingOnStreams() {
		class Item {

			private String name;
			private int qty;
			private double price;
			
			// constructors, getter/setters
			public Item(String name, int qty, double price) {
				this.name = name;
				this.qty = qty;
				this.price = price;
			}
			public String getName() {
				return name;
			}
			public int getQty() {
				return qty;
			}
			public double getPrice() {
				return price;
			}
		}
		
		//3 apple, 2 banana, others 1
        Item[] items = new Item[] {
                new Item("apple", 10, 9.99),
                new Item("banana", 20, 19.99),
                new Item("orange", 10, 29.99),
                new Item("watermelon", 10, 29.99),
                new Item("papaya", 20, 9.99),
                new Item("apple", 10, 9.99),
                new Item("banana", 10, 19.99),
                new Item("apple", 20, 9.99)
        };
        
        // Group by the name + Count of Items.
        System.out.println("Group by the name + Count of Items");
        Map<String, Long> groupByCountMap = Stream.of(items).collect(
                Collectors.groupingBy(Item::getName, Collectors.counting()));

        System.out.println(groupByCountMap);

        // Group by the name + Sum the Qty.
        System.out.println("Group by the name + Sum the Qty");
        Map<String, Integer> groupbByNameAndSumMap = Stream.of(items).collect(
                Collectors.groupingBy(Item::getName, Collectors.summingInt(Item::getQty)));

        System.out.println(groupbByNameAndSumMap);

        // Group by the name and Avergae Price per pound of the item.
        System.out.println("Group by the name + Sum the Qty");
        Map<String, Double> groupbByNameAndAvgMap = Stream.of(items).collect(
                Collectors.groupingBy(Item::getName, Collectors.averagingDouble(item -> item.getPrice() / item.getQty())));

        System.out.println(groupbByNameAndAvgMap);

        // Group Items by prices
        System.out.println("Group Items by prices and return a List");
        Map<Double, List<Item>> groupItemsAsListByPriceMap =
    		   Stream.of(items).collect(Collectors.groupingBy(Item::getPrice));

        System.out.println(groupItemsAsListByPriceMap);
        
       // Group by price, uses 'mapping' to convert List<Item> to Set<String>
       System.out.println("Group Items by prices and return a Set");
       Map<Double, Set<String>> groupItemsAsSetByPriceMap =
    		   Stream.of(items).collect(
                        Collectors.groupingBy(Item::getPrice,
                                Collectors.mapping(Item::getName, Collectors.toSet())
                        )
                );

        System.out.println(groupItemsAsSetByPriceMap);
        
        // Group by price, uses 'mapping' to convert List<Item> to HashSet<String>
        // In the previous example there are no guarantees about what type of Set is returned. However, using 
        // toCollection() we can have more control. For example, we can ask for a HashSet by passing a constructor 
        // reference to it
        System.out.println("Group Items by prices and return a HashSet");
        Map<Double, HashSet<String>> groupItemsAsHashSetByPriceMap =
     		   Stream.of(items).collect(
                         Collectors.groupingBy(Item::getPrice,
                                 Collectors.mapping(Item::getName, Collectors.toCollection(HashSet::new))
                         )
                 );

         System.out.println(groupItemsAsHashSetByPriceMap);
         
         System.out.println("Group Items by prices into two groups: inexpensive items and non inexpensive items");
         Map<Boolean, Set<String>> partitionItemsAsExpensiveInExpensiveMap =
      		   Stream.of(items).collect(
                          Collectors.partitioningBy(item -> item.getPrice() > 19.99,
                                  Collectors.mapping(Item::getName, Collectors.toSet())
                          )
                  );

          System.out.println(partitionItemsAsExpensiveInExpensiveMap);
	}
	
	public static void filteringAMap() {
		
		Map<Integer, String> hosting = new HashMap<Integer, String>();
		hosting.put(1, "linode.com");
		hosting.put(2, "heroku.com");
		hosting.put(3, "digitalocean.com");
		hosting.put(3, "aws.amazon.com");
        
        //Map -> Stream -> Filter -> String
        String result = hosting.entrySet().stream()
                .filter(entry -> "aws.amazon.com".equals(entry.getValue()))
                .map(entry -> entry.getValue())
                .collect(Collectors.joining());

        System.out.println("With Java 8 : Map -> Stream -> Filter -> String: " + result);
        
        //Map -> Stream -> Filter -> Map
        Map<Integer, String> collect = hosting.entrySet().stream()
                .filter(entry -> entry.getKey() == 2)
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));

        System.out.println("With Java 8 : Map -> Stream -> Filter -> Map: " + collect); //output : {2=heroku.com}
	}
	
	public static void filteringAStream() {
		
		class Person {
			
			private String name;
			private int age;
			
			public String getName() {
				return name;
			}

			public Person(String name, int age) {
				this.name = name;
				this.age = age;
			}

			public int getAge() {
				return age;
			}		
		}

		// Streams filter() and collect()
		// stream.filter() to filter a List, and collect() to convert a stream.
		List<String> lines = Arrays.asList("spring", "node", "mkyong");

		List<String> result = lines.stream() 		//convert list to stream
			.filter(line -> !"mkyong".equals(line))	//filters the line, equals to "mkyong"
			.collect(Collectors.toList());			//collect the output and convert streams to a List

		result.forEach(System.out::println);		//output : spring node
		
		// Streams filter(), findAny() and orElse() with single condition
		List<Person> persons = Arrays.asList(new Person("mkyong", 65), 
											 new Person("michael", 17), 
											 new Person("lawrence", 34));

		Person result2 = persons.stream()				   // Convert to steam
			.filter(x -> "michael".equals(x.getName()))	   // we want "michael" only
			.findAny()									   // If 'findAny' then return found
			.orElse(null);								   // If not found, return null
		
		// Streams filter(), findAny() and orElse() with multiple conditions
		Person result3 = persons.stream()
				.filter((x) -> "michael".equals(x.getName()) && 21 == x.getAge())
				.findAny()
				.orElse(null);

		//or like this
		Person result4 = persons.stream()
			.filter(x -> {
				if("michael".equals(x.getName()) && 21 == x.getAge()){
					return true;
				}
				return false;
			}).findAny()
			.orElse(null);
		
		 // Extra, filter() and map() example.
		String name = persons.stream()
				.filter(x -> "michael".equals(x.getName()))
				.map(Person::getName)						//convert stream to String
				.findAny()
				.orElse("");
		
		// Find oldest person
		Optional<Person> oldest = persons.stream()
						.collect(Collectors.maxBy(Comparator.comparing(Person::getAge)));
		System.out.println(oldest.isPresent() ? oldest.get().getAge() : -1);
	}

	public static void flatMap() {
		
		// 1. Stream + String[] + flatMap
		String[][] data = new String[][]{{"a", "b"}, {"c", "d"}, {"e", "f"}};

        //Stream<String[]>
        Stream<String[]> temp = Arrays.stream(data);

        //Stream<String>, GOOD!
        Stream<String> stringStream = temp.flatMap(x -> Arrays.stream(x));
        
        Stream<String> stream = stringStream.filter(x -> "a".equals(x.toString()));

        stream.forEach(System.out::println);
        
        /*Stream<String> stream = Arrays.stream(data)
        	.flatMap(x -> Arrays.stream(x))
        	.filter(x -> "a".equals(x.toString()));*/
        
        
        // 2. Stream + Set + flatMap
        class Student {

            private String name;
            private Set<String> book;

            public void addBook(String book) {
                if (this.book == null) {
                    this.book = new HashSet<>();
                }
                this.book.add(book);
            }
            //getters and setters

			public String getName() {
				return name;
			}

			public void setName(String name) {
				this.name = name;
			}

			public Set<String> getBook() {
				return book;
			}

			public void setBook(Set<String> book) {
				this.book = book;
			}
        }
		
        
        // Collect unique vook names
		Student obj1 = new Student();
        obj1.setName("mkyong");
        obj1.addBook("Java 8 in Action");
        obj1.addBook("Spring Boot in Action");
        obj1.addBook("Effective Java (2nd Edition)");

        Student obj2 = new Student();
        obj2.setName("zilap");
        obj2.addBook("Learning Python, 5th Edition");
        obj2.addBook("Effective Java (2nd Edition)");

        List<Student> list = new ArrayList<>();
        list.add(obj1);
        list.add(obj2);

        List<String> collect =
                list.stream()
                        .map(x -> x.getBook())      //Stream<Set<String>>
                        .flatMap(x -> x.stream())   //Stream<String>
                        .distinct()
                        .collect(Collectors.toList());

        collect.forEach(x -> System.out.println(x));
        
        // 3. Stream + Primitive + flatMapToInt
        int[] intArray = {1, 2, 3, 4, 5, 6};

        //1. Stream<int[]>
        Stream<int[]> streamArray = Stream.of(intArray);

        //2. Stream<int[]> -> flatMap -> IntStream
        IntStream intStream = streamArray.flatMapToInt(x -> Arrays.stream(x));

        intStream.forEach(x -> System.out.println(x));
	}
	
	public static void performanceBenchMarking(){
		
		// This program calculates the sum of 100 million numbers, once through sequential stream and the other through parallel stream		
		long start = System.currentTimeMillis();
		long sum = LongStream.rangeClosed(1,  100000000).sum();
		long end = System.currentTimeMillis();
		System.out.println("Time taken for range : " + (end - start));
		
		start = System.currentTimeMillis();
		sum = LongStream.rangeClosed(1,  100000000).parallel().sum();
		end = System.currentTimeMillis();
		System.out.println("Time taken for parallel range : " + (end - start));
	}
	
	public static void main(String[] args) {
		//sortMapByKeyByAndValue();
		//convertStreamToList();
		//groupingByOnStreams();
		//convertMaptToList();
		//streamCannotBeReused();
		//resusingAStream();
		//groupingAndSortingOnStream();
		//groupCountMappingOnStreams();
		//convertListToMap();
		//filteringAMap();
		performanceBenchMarking();
	}
}
