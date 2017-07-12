package fr.next.numericalimage.service;

import java.util.Map;

public interface Service {

	Map<String, Object> process(Map<String, Object> parameters, Map<String, Object> args);
}
