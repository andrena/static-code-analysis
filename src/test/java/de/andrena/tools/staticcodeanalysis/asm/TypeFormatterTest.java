package de.andrena.tools.staticcodeanalysis.asm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class TypeFormatterTest {

    private final TypeFormatter formatter = new TypeFormatter();

    @Test
    void simpleMethodsWithoutArgument() {
        assertThat(formatter.formatMethodSignature("simple", "()V")).isEqualTo("void simple()");
        assertThat(formatter.formatMethodSignature("simpleReturn", "()I")).isEqualTo("int simpleReturn()");
    }

    @Test
    void simpleMethodsWithArgument() {
        assertThat(formatter.formatMethodSignature("simple", "(I)V")).isEqualTo("void simple(int)");
    }

    @Test
    void simpleMethodsWithReturnClass() {
        assertThat(formatter.formatMethodSignature("simple", "()Ljava/util/List;")).isEqualTo("List simple()");
    }

    @Test
    void methodWithArrays() {
        assertThat(formatter.formatMethodSignature("mapValues", "([Lde/andrena/tools/staticcodeanalysis/sample/Value;)[Lde/andrena/tools/staticcodeanalysis/sample/Value;")).isEqualTo("Value[] mapValues(Value[])");
    }

    @Test
    void methodWithVarArg() {
        assertThat(formatter.formatMethodSignature("mapValues", "(Z[Ljava/lang/String;)J")).isEqualTo("long mapValues(boolean,String[])");
    }

    @Test
    void complexMethods() {
        assertThat(formatter.formatMethodSignature("checkValue", "(Lde/andrena/tools/staticcodeanalysis/sample/Value;)Z")).isEqualTo("boolean checkValue(Value)");
        assertThat(formatter.formatMethodSignature("checkValue", "(Lde/andrena/tools/staticcodeanalysis/sample/Value;Ljava/lang/String;)Z")).isEqualTo("boolean checkValue(Value,String)");
        assertThat(formatter.formatMethodSignature("checkValue", "(Lde/andrena/tools/staticcodeanalysis/sample/Value;Ljava/util/List;I)Z")).isEqualTo("boolean checkValue(Value,List,int)");
    }

    @Test
    void methodWithAllArguments() {

        assertThat(formatter.formatMethodSignature("mapValues", "(Ljava/util/List;ZBCIJDFS[Ljava/lang/String;Ljava/lang/Boolean;)Ljava/util/List;")).isEqualTo("List mapValues(List,boolean,byte,char,int,long,double,float,short,String[],Boolean)");
    }

    @Test
    void invalidSignatureThrows() {
        assertThatThrownBy(() -> formatter.formatMethodSignature("mapValues", "(Ljava)Z"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> formatter.formatMethodSignature("mapValues", ""))
                .isInstanceOf(StringIndexOutOfBoundsException.class);
    }

    @Test
    void simpleTypes() {
        assertThat(formatter.formatType("I")).isEqualTo("int");
        assertThat(formatter.formatType("J")).isEqualTo("long");
        assertThat(formatter.formatType("Z")).isEqualTo("boolean");
    }

    @Test
    void classTypes() {
        assertThat(formatter.formatType("Ljava.lang.String;")).isEqualTo("String");
        assertThat(formatter.formatType("Lcom.x.Class;")).isEqualTo("Class");
    }
    @Test
    void arrayTypes() {
        assertThat(formatter.formatType("[I")).isEqualTo("int[]");
    }

}
