package dblp;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PaperPredicate{
	public static Predicate<JsonObject> containKeyword(String keyword) {
		return p -> p.get("title").getAsString().toLowerCase().contains(keyword.toLowerCase());

	}

	public static Predicate<JsonObject> findPaper(String id) {
		return p -> p.get("id").getAsString().contentEquals(id);

	}

	public static List<JsonObject> filterPapers (List<JsonObject> papers, Predicate<JsonObject> predicate) {
		return papers.stream()
				.filter(predicate)
				.collect(Collectors.<JsonObject>toList());
	}
	public static List<JsonObject>  findPapers (List<JsonObject> papers, Predicate<JsonObject> predicate) {
		return papers.stream()
				.filter(predicate)
				.collect(Collectors.<JsonObject>toList());
	}

}