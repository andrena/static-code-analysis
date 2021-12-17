package de.andrena.tools.staticcodeanalysis.domain.model;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ClassReferenceTest {
	private static final String CLASS_NAME = "de.package.Class";
	private ClassReference myClass = new ClassReference(CLASS_NAME);
	private ClassReference otherClass = new ClassReference("de.otherPackage.OtherClass");

	@Test
	void classNameMatches() {
		assertThat(myClass.classNameMatches("de.*")).isTrue();
		assertThat(myClass.classNameMatches(".*Class")).isTrue();
		assertThat(myClass.classNameMatches("Class")).isFalse();
	}

	@Test
	void getFullName() {
		assertThat(myClass.getFullName()).isEqualTo(CLASS_NAME);
	}

	@Test
	void getShortName() {
		assertThat(myClass.getShortName("de")).isEqualTo("package.Class");
		assertThat(myClass.getShortName("de.")).isEqualTo("package.Class");
		assertThat(myClass.getShortName("de.package")).isEqualTo("Class");
		assertThat(myClass.getShortName("com.package")).isEqualTo(CLASS_NAME);
		assertThat(myClass.getShortName("de.p")).isEqualTo(CLASS_NAME);
	}

	@Test
	void isInPackage() {
		assertThat(myClass.isInPackage("de")).isTrue();
		assertThat(myClass.isInPackage("de.")).isTrue();
		assertThat(myClass.isInPackage("de.package")).isTrue();
		assertThat(myClass.isInPackage("de.p")).isFalse();
		assertThat(myClass.isInPackage("com")).isFalse();
		assertThat(myClass.isInPackage("package")).isFalse();
	}

	@Test
	void compareTo() {
		assertThat(myClass.compareTo(myClass)).isZero();
		assertThat(myClass.compareTo(new ClassReference(CLASS_NAME))).isZero();
		assertThat(myClass.compareTo(otherClass)).isEqualTo(1);
		assertThat(otherClass.compareTo(myClass)).isEqualTo(-1);
	}

}