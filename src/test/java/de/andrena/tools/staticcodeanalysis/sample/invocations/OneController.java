package de.andrena.tools.staticcodeanalysis.sample.invocations;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class OneController extends  SuperController {
    private InvocationTestService mapper = new InvocationTestService();
    private PersonService persons = new PersonService();
    private SampleService sampleService = new SampleService();

    public void mapAndLoad() {
        map(111);
        sampleService.map(12);
        persons.load();
    }
    public void mapAndSave() {
        map(123);
        sampleService.map(12L);
        persons.save();
    }

    public void callsToNewlyCreatedService() {
        var service = new AccessibilityTestService();
        service.publicMethodIsConsidered();
        service.protectedMethodIsConsidered();
        service.packagePrivateMethodIsConsidered();
    }

    public void lambdaCalling() {
        Stream.of(1,2,3).forEach(mapper::calledFromLambda);
    }
    public void anonymousClassCalling() {

        Stream.of(1,2,3).forEach(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) {
                mapper.calledFromAnonymousClass();
            }
        });
    }

    public void find() {
        persons.find();
    }

    private void map(int value) {
        mapper.map(value);
    }

    private void deadMethodIsRegarded() {
        persons.update();
    }

    private class InnerClass {
        public void callMapper() {
            mapper.calledFromInnerClass();
        }
    }

}
