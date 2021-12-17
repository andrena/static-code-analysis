package de.andrena.tools.staticcodeanalysis.asm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.Test;

class MethodSignatureFormatterTest {

    private final MethodSignatureFormatter formatter = new MethodSignatureFormatter();

    @Test
    void simpleMethodsWithoutArgument() {
        assertThat(formatter.format("simple","()V")).isEqualTo("void simple()");
        assertThat(formatter.format("simpleReturn","()I")).isEqualTo("int simpleReturn()");
    }

    @Test
    void simpleMethodsWithArgument() {
        assertThat(formatter.format("simple","(I)V")).isEqualTo("void simple(int)");
    }

    @Test
    void simpleMethodsWithReturnClass() {
        assertThat(formatter.format("simple","()Ljava/util/List;")).isEqualTo("List simple()");
    }

    @Test
    void methodWithArrays() {
        assertThat(formatter.format("mapValues","([Lde/andrena/tools/staticcodeanalysis/sample/Value;)[Lde/andrena/tools/staticcodeanalysis/sample/Value;")).isEqualTo("Value[] mapValues(Value[])");
    }
    @Test
    void methodWithVarArg() {
        assertThat(formatter.format("mapValues","(Z[Ljava/lang/String;)J")).isEqualTo("long mapValues(boolean,String[])");
    }

    @Test
    void complexMethods() {
        assertThat(formatter.format("checkValue","(Lde/andrena/tools/staticcodeanalysis/sample/Value;)Z")).isEqualTo("boolean checkValue(Value)");
        assertThat(formatter.format("checkValue","(Lde/andrena/tools/staticcodeanalysis/sample/Value;Ljava/lang/String;)Z")).isEqualTo("boolean checkValue(Value,String)");
        assertThat(formatter.format("checkValue","(Lde/andrena/tools/staticcodeanalysis/sample/Value;Ljava/util/List;I)Z")).isEqualTo("boolean checkValue(Value,List,int)");
    }
    @Test
    void methodWithAllArguments() {

        assertThat(formatter.format("mapValues","(Ljava/util/List;ZBCIJDFS[Ljava/lang/String;Ljava/lang/Boolean;)Ljava/util/List;")).isEqualTo("List mapValues(List,boolean,byte,char,int,long,double,float,short,String[],Boolean)");
    }

    @Test
    void invalidSignatureThrows() {
        assertThatThrownBy(() -> formatter.format("mapValues","(Ljava)Z"))
                .isInstanceOf(IllegalArgumentException.class);
        assertThatThrownBy(() -> formatter.format("mapValues",""))
                .isInstanceOf(StringIndexOutOfBoundsException.class);
    }

}
