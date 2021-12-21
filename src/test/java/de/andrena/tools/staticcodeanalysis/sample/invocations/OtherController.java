package de.andrena.tools.staticcodeanalysis.sample.invocations;

@SuppressWarnings("all")
public class OtherController extends SuperController {
    private InvocationTestService mapper = new InvocationTestService();
    private SampleService sampleService = new SampleService();

    public void mapAndLoad() {
        mapper.map(111);
        mapper.map(111L);
        mapper.map((char)111);
        sampleService.build(2,false);
        sampleService.build(2L,false);
    }
}
