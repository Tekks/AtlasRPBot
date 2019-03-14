package wtf.tks.bots.Backend;

import java.io.*;
import java.util.*;


@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
public final class OrderedProperties implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private transient Map<String, String> properties;
	private transient boolean suppressDate;
	
	
	public OrderedProperties() {
		this(new LinkedHashMap<String, String>(), false);
	}
	
	private OrderedProperties(Map<String, String> properties, boolean suppressDate) {
		this.properties = properties;
		this.suppressDate = suppressDate;
	}
	
	public static OrderedProperties copyOf(OrderedProperties source) {
		// create a copy that has the same behaviour
		OrderedPropertiesBuilder builder = new OrderedPropertiesBuilder();
		builder.withSuppressDateInComment(source.suppressDate);
		if (source.properties instanceof TreeMap) {
			builder.withOrdering(((TreeMap<String, String>) source.properties).comparator());
		}
		OrderedProperties result = builder.build();
		
		// copy the properties from the source to the target
		for (Map.Entry<String, String> entry : source.entrySet()) {
			result.setProperty(entry.getKey(), entry.getValue());
		}
		return result;
	}
	
	public String getProperty(String key) {
		return properties.get(key);
	}
	
	public String getProperty(String key, String defaultValue) {
		String value = properties.get(key);
		return (value == null) ? defaultValue : value;
	}
	
	public String setProperty(String key, String value) {
		return properties.put(key, value);
	}
	
	public String removeProperty(String key) {
		return properties.remove(key);
	}
	
	public boolean containsProperty(String key) {
		return properties.containsKey(key);
	}
	
	public int size() {
		return properties.size();
	}
	
	public boolean isEmpty() {
		return properties.isEmpty();
	}
	
	public Enumeration<String> propertyNames() {
		return new Vector<String>(properties.keySet()).elements();
	}
	
	public Set<String> stringPropertyNames() {
		return new LinkedHashSet<String>(properties.keySet());
	}
	
	public Set<Map.Entry<String, String>> entrySet() {
		return new LinkedHashSet<Map.Entry<String, String>>(properties.entrySet());
	}
	
	public void load(InputStream stream) throws IOException {
		CustomProperties customProperties = new CustomProperties(this.properties);
		customProperties.load(stream);
	}
	
	public void load(Reader reader) throws IOException {
		CustomProperties customProperties = new CustomProperties(this.properties);
		customProperties.load(reader);
	}
	
	@SuppressWarnings("DuplicateThrows")
	public void loadFromXML(InputStream stream)
			throws IOException, InvalidPropertiesFormatException {
		CustomProperties customProperties = new CustomProperties(this.properties);
		customProperties.loadFromXML(stream);
	}
	
	public void store(OutputStream stream, String comments) throws IOException {
		CustomProperties customProperties = new CustomProperties(this.properties);
		if (suppressDate) {
			customProperties.store(new DateSuppressingPropertiesBufferedWriter(
					new OutputStreamWriter(stream, "8859_1")), comments);
		} else {
			customProperties.store(stream, comments);
		}
	}
	
	public void store(Writer writer, String comments) throws IOException {
		CustomProperties customProperties = new CustomProperties(this.properties);
		if (suppressDate) {
			customProperties.store(new DateSuppressingPropertiesBufferedWriter(writer), comments);
		} else {
			customProperties.store(writer, comments);
		}
	}
	
	public void storeToXML(OutputStream stream, String comment) throws IOException {
		CustomProperties customProperties = new CustomProperties(this.properties);
		customProperties.storeToXML(stream, comment);
	}
	
	public void storeToXML(OutputStream stream, String comment, String encoding)
			throws IOException {
		CustomProperties customProperties = new CustomProperties(this.properties);
		customProperties.storeToXML(stream, comment, encoding);
	}
	
	public void list(PrintStream stream) {
		CustomProperties customProperties = new CustomProperties(this.properties);
		customProperties.list(stream);
	}
	
	public void list(PrintWriter writer) {
		CustomProperties customProperties = new CustomProperties(this.properties);
		customProperties.list(writer);
	}
	
	public Properties toJdkProperties() {
		Properties jdkProperties = new Properties();
		for (Map.Entry<String, String> entry : this.entrySet()) {
			jdkProperties.put(entry.getKey(), entry.getValue());
		}
		return jdkProperties;
	}
	
	@Override
	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		
		if (other == null || getClass() != other.getClass()) {
			return false;
		}
		
		OrderedProperties that = (OrderedProperties) other;
		return Arrays.equals(properties.entrySet().toArray(), that.properties.entrySet().toArray());
	}
	
	@Override
	public int hashCode() {
		return Arrays.hashCode(properties.entrySet().toArray());
	}
	
	private void writeObject(ObjectOutputStream stream) throws IOException {
		stream.defaultWriteObject();
		stream.writeObject(properties);
		stream.writeBoolean(suppressDate);
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream stream) throws IOException, ClassNotFoundException {
		stream.defaultReadObject();
		properties = (Map<String, String>) stream.readObject();
		suppressDate = stream.readBoolean();
	}
	
	private void readObjectNoData() throws InvalidObjectException {
		throw new InvalidObjectException("Stream data required");
	}
	
	@Override
	public String toString() {
		return properties.toString();
	}
	
	public static final class OrderedPropertiesBuilder {
		
		private Comparator<? super String> comparator;
		private boolean suppressDate;
		
		
		public OrderedPropertiesBuilder withOrdering(Comparator<? super String> comparator) {
			this.comparator = comparator;
			return this;
		}
		
		
		public OrderedPropertiesBuilder withSuppressDateInComment(boolean suppressDate) {
			this.suppressDate = suppressDate;
			return this;
		}
		
		
		public OrderedProperties build() {
			Map<String, String> properties = (this.comparator != null) ?
					new TreeMap<String, String>(comparator) :
					new LinkedHashMap<String, String>();
			return new OrderedProperties(properties, suppressDate);
		}
		
	}
	
	
	private static final class CustomProperties extends Properties {
		
		private final Map<String, String> targetProperties;
		
		private CustomProperties(Map<String, String> targetProperties) {
			this.targetProperties = targetProperties;
		}
		
		@Override
		public Object get(Object key) {
			return targetProperties.get(key);
		}
		
		@Override
		public Object put(Object key, Object value) {
			return targetProperties.put((String) key, (String) value);
		}
		
		@Override
		public String getProperty(String key) {
			return targetProperties.get(key);
		}
		
		@Override
		public Enumeration<Object> keys() {
			return new Vector<Object>(targetProperties.keySet()).elements();
		}
		
		@SuppressWarnings("NullableProblems")
		@Override
		public Set<Object> keySet() {
			return new LinkedHashSet<Object>(targetProperties.keySet());
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public Set<Map.Entry<Object, Object>> entrySet() {
			Set entrySet = targetProperties.entrySet();
			return (Set<Map.Entry<Object, Object>>) entrySet;
		}
		
	}
	
	
	private static final class DateSuppressingPropertiesBufferedWriter extends BufferedWriter {
		
		private final String LINE_SEPARATOR = System.getProperty("line.separator");
		
		private StringBuilder currentComment;
		private String previousComment;
		
		private DateSuppressingPropertiesBufferedWriter(Writer out) {
			super(out);
		}
		
		@SuppressWarnings("NullableProblems")
		@Override
		public void write(String string) throws IOException {
			if (currentComment != null) {
				currentComment.append(string);
				if (string.endsWith(LINE_SEPARATOR)) {
					if (previousComment != null) {
						super.write(previousComment);
					}
					
					previousComment = currentComment.toString();
					currentComment = null;
				}
			} else if (string.startsWith("#")) {
				currentComment = new StringBuilder(string);
			} else {
				super.write(string);
			}
		}
		
	}
	
}
