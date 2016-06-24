/*
 * Copyright 2014-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package example.springdata.multistore.gemstone;

import java.math.BigDecimal;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.gemfire.mapping.Region;
import org.springframework.util.ObjectUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The Gemstone class is an ADT modeling an actual Gemstone.
 *
 * @author John Blum
 * @see org.springframework.data.gemfire.mapping.Region
 */
@NoArgsConstructor
@Region("Gemstones")
@SuppressWarnings("unused")
public class Gemstone {

	protected static final String UNKNOWN = "Unknown";

	@Getter @Setter
	private BigDecimal value;

	@Setter
	private Long id;

	@Getter @Setter
	private String name;

	public static Gemstone newGemstone() {
		return newGemstone(UNKNOWN);
	}

	public static Gemstone newGemstone(String name) {
		Gemstone gemstone = new Gemstone();
		gemstone.setId(generateId());
		gemstone.setName(name);
		return gemstone;
	}

	protected static long generateId() {
		UUID uuid = UUID.randomUUID();

		return Math.abs(uuid.getMostSignificantBits() | uuid.getLeastSignificantBits());
	}

	@Id
	public Long getId() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof Gemstone)) {
			return false;
		}

		Gemstone that = (Gemstone) obj;

		return ObjectUtils.nullSafeEquals(this.getId(), that.getId())
			&& ObjectUtils.nullSafeEquals(this.getName(), that.getName());
	}

	@Override
	public int hashCode() {
		int hashValue = 17;
		hashValue = 37 * hashValue + ObjectUtils.nullSafeHashCode(this.getId());
		hashValue = 37 * hashValue + ObjectUtils.nullSafeHashCode(this.getName());
		return hashValue;
	}

	@Override
	public String toString() {
		return String.format("{ @type = %1$s, id = %2$d, name = %3$s, value = %4$s }",
			getClass().getName(), getId(), getName(), getValue());
	}
}
