/*
	Copyright 2010 Massachusetts General Hospital

	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at

	    http://www.apache.org/licenses/LICENSE-2.0

	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.
*/
package org.sc.probro;

import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONString;
import org.json.JSONStringer;

public class Request extends BrokerData {
	
	public String id;  // provisional_term
	
	public String ontology_term;
	
	public String search_text;
	public String context;
	public String comment;
	public String status;
	public String provenance;
	
	public User creator;
	public User modified_by;
	public String date_submitted;
	public Ontology ontology;
	public ArrayList<Metadata> metadata;
	
	public Request() {}
	
	public Request(JSONObject obj) throws JSONException { 
		id = obj.getString("provisional_term");
		ontology_term = obj.getString("ontology_term");
		search_text = obj.getString("search_text");
		context = obj.getString("context");
		comment = obj.getString("comment");
		status = obj.getString("status");
		provenance = obj.getString("provenance");
		date_submitted = obj.getString("date_submitted");
		
		creator = new User(obj.getJSONObject("creator"));
		modified_by = new User(obj.getJSONObject("modified_by"));
		ontology = new Ontology(obj.getJSONObject("ontology"));
		
		metadata = new ArrayList<Metadata>();
		JSONArray marray = obj.getJSONArray("metadata");
		
		for(int i = 0; i < marray.length(); i++) { 
			metadata.add(new Metadata(marray.getJSONObject(i)));
		}
	}
	
	public String toString() { return id; }

	public void stringJSON(JSONStringer stringer) throws JSONException { 
		
		stringer.object();
		
		stringer.key("provisional_term").value(id);
		stringer.key("ontology_term").value(ontology_term);
		stringer.key("search_text").value(search_text);
		stringer.key("context").value(context);
		stringer.key("comment").value(comment);
		stringer.key("provenance").value(provenance);
		stringer.key("status").value(status);
		
		stringer.key("creator");
		creator.stringJSONLink(stringer);
		
		stringer.key("modified_by");
		modified_by.stringJSONLink(stringer);
		
		stringer.key("date_submitted").value(date_submitted);
		
		stringer.key("ontology");
		ontology.stringJSONLink(stringer);
		
		stringer.key("metadata");
		stringer.array();
		for(Metadata m : metadata) { 
			m.stringJSON(stringer);
		}
		stringer.endArray();
		
		stringer.endObject();
		
	}

	public Map<String, Set<String>> createMetadataMap() {
		Map<String,Set<String>> map = new TreeMap<String,Set<String>>();
		for(Metadata m : metadata) { 
			if(!map.containsKey(m.key)) { 
				map.put(m.key, new TreeSet<String>());
			}
			map.get(m.key).add(m.value);
		}
		return map;
	}
}
