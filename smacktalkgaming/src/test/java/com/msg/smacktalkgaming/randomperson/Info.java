package com.msg.smacktalkgaming.randomperson;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "seed", "results", "page", "version" })
public class Info {

	@JsonProperty("seed")
	private String seed;
	@JsonProperty("results")
	private int results;
	@JsonProperty("page")
	private int page;
	@JsonProperty("version")
	private String version;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	@JsonProperty("seed")
	public String getSeed() {
		return seed;
	}

	@JsonProperty("seed")
	public void setSeed(String seed) {
		this.seed = seed;
	}

	@JsonProperty("results")
	public int getResults() {
		return results;
	}

	@JsonProperty("results")
	public void setResults(int results) {
		this.results = results;
	}

	@JsonProperty("page")
	public int getPage() {
		return page;
	}

	@JsonProperty("page")
	public void setPage(int page) {
		this.page = page;
	}

	@JsonProperty("version")
	public String getVersion() {
		return version;
	}

	@JsonProperty("version")
	public void setVersion(String version) {
		this.version = version;
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}
