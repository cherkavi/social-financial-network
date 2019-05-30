package bc.payment.citypay.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import junit.framework.Assert;

public class CityPayServiceTest {

	@Test
	public void extractValueFromFieldMap(){
		// given
		Map<String, String> parameters=new HashMap<String, String>();
		parameters.put("field2", "value2");
		parameters.put("field4", "value4");
		parameters.put("field3", "value3");
		parameters.put("field1", "value1");
		
		// when
		List<Pair<String, String>> values=CityPayService.Operation.extractOrderedFields("field", parameters);
		
		// then
		Assert.assertEquals(4, values.size());
		Assert.assertEquals("field1", values.get(0).getKey());
		Assert.assertEquals("value1", values.get(0).getValue());

		Assert.assertEquals("field2", values.get(1).getKey());
		Assert.assertEquals("value2", values.get(1).getValue());

		Assert.assertEquals("field3", values.get(2).getKey());
		Assert.assertEquals("value3", values.get(2).getValue());

		Assert.assertEquals("field4", values.get(3).getKey());
		Assert.assertEquals("value4", values.get(3).getValue());
	}
	
	@Test
	public void extractValueFromFieldMapWithMess(){
		// given
		Map<String, String> parameters=new HashMap<String, String>();
		parameters.put("field2", "value2");
		parameters.put("field4", "value4");
		parameters.put("field3", "value3");
		parameters.put("field1", "value1");
		parameters.put("bla-bla1", "value1");
		parameters.put("mess1", "value1");
		
		// when
		List<Pair<String, String>> values=CityPayService.Operation.extractOrderedFields("field", parameters);
		
		// then
		Assert.assertEquals(4, values.size());
		Assert.assertEquals("field1", values.get(0).getKey());
		Assert.assertEquals("value1", values.get(0).getValue());

		Assert.assertEquals("field2", values.get(1).getKey());
		Assert.assertEquals("value2", values.get(1).getValue());

		Assert.assertEquals("field3", values.get(2).getKey());
		Assert.assertEquals("value3", values.get(2).getValue());

		Assert.assertEquals("field4", values.get(3).getKey());
		Assert.assertEquals("value4", values.get(3).getValue());
	}

	@Test
	public void extractValueFromFieldMapWithEmptyFields(){
		// given
		Map<String, String> parameters=new HashMap<String, String>();
		parameters.put("field2", "value2");
		parameters.put("field4", "value4");
		parameters.put("field3", "value3");
		parameters.put("field7", "value7");
		parameters.put("field8", "value8");
		
		// when
		List<Pair<String, String>> values=CityPayService.Operation.extractOrderedFields("field", parameters);
		
		// then
		Assert.assertEquals(8, values.size());
		Assert.assertEquals("field1", values.get(0).getKey());
		Assert.assertNull(values.get(0).getValue());
		Assert.assertEquals("field2", values.get(1).getKey());
		Assert.assertEquals("value2", values.get(1).getValue());
		Assert.assertEquals("field3", values.get(2).getKey());
		Assert.assertEquals("value3", values.get(2).getValue());
		Assert.assertEquals("field4", values.get(3).getKey());
		Assert.assertEquals("value4", values.get(3).getValue());
		Assert.assertEquals("field5", values.get(4).getKey());
		Assert.assertNull(values.get(4).getValue());
		Assert.assertEquals("field6", values.get(5).getKey());
		Assert.assertNull(values.get(5).getValue());
		Assert.assertEquals("field7", values.get(6).getKey());
		Assert.assertEquals("value7", values.get(6).getValue());
		Assert.assertEquals("field8", values.get(7).getKey());
		Assert.assertEquals("value8", values.get(7).getValue());
	}

	@Test
	public void extractValueFromFieldMapWithNegativeNumbers(){
		// given
		Map<String, String> parameters=new HashMap<String, String>();
		parameters.put("field-2", "value2");
		parameters.put("field4", "value4");
		parameters.put("field3", "value3");
		parameters.put("field-1", "value1");
		
		// when
		List<Pair<String, String>> values=CityPayService.Operation.extractOrderedFields("field", parameters);
		
		// then
		Assert.assertEquals(4, values.size());
		Assert.assertEquals("field1", values.get(0).getKey());
		Assert.assertNull(values.get(0).getValue());
		Assert.assertEquals("field2", values.get(1).getKey());
		Assert.assertNull(values.get(1).getValue());
		Assert.assertEquals("field3", values.get(2).getKey());
		Assert.assertEquals("value3", values.get(2).getValue());
		Assert.assertEquals("field4", values.get(3).getKey());
		Assert.assertEquals("value4", values.get(3).getValue());
	}

	@Test
	public void extractValueFromFieldMapWithHugeNumbers(){
		// given
		Map<String, String> parameters=new HashMap<String, String>();
		parameters.put("field1", "value1");
		parameters.put("field2", "value2");
		parameters.put("field3", "value3");
		parameters.put("field4", "value4");
		parameters.put("field5", "value5");
		parameters.put("field6", "value6");
		parameters.put("field18", "value18");
		parameters.put("field19", "value19");
		parameters.put("field20", "value20");
		parameters.put("field21", "value21");
		parameters.put("field22", "value22");
		
		// when
		List<Pair<String, String>> values=CityPayService.Operation.extractOrderedFields("field", parameters);
		
		// then
		Assert.assertEquals(CityPayService.Operation.MAX_EXTRA_FIELD_INDEX, values.size());
		Assert.assertEquals("field1", values.get(0).getKey());
		Assert.assertEquals("value1", values.get(0).getValue());
		Assert.assertEquals("field2", values.get(1).getKey());
		Assert.assertEquals("value2", values.get(1).getValue());
		Assert.assertEquals("field3", values.get(2).getKey());
		Assert.assertEquals("value3", values.get(2).getValue());
		Assert.assertEquals("field4", values.get(3).getKey());
		Assert.assertEquals("value4", values.get(3).getValue());
		Assert.assertEquals("field5", values.get(4).getKey());
		Assert.assertEquals("value5", values.get(4).getValue());
		Assert.assertEquals("field6", values.get(5).getKey());
		Assert.assertEquals("value6", values.get(5).getValue());
		Assert.assertEquals("field7", values.get(6).getKey());
		Assert.assertNull(values.get(6).getValue());
		Assert.assertEquals("field8", values.get(7).getKey());
		Assert.assertNull(values.get(7).getValue());
		Assert.assertEquals("field9", values.get(8).getKey());
		Assert.assertNull(values.get(8).getValue());
		Assert.assertEquals("field10", values.get(9).getKey());
		Assert.assertNull(values.get(9).getValue());
		Assert.assertEquals("field11", values.get(10).getKey());
		Assert.assertNull(values.get(10).getValue());
		Assert.assertEquals("field12", values.get(11).getKey());
		Assert.assertNull(values.get(11).getValue());
		Assert.assertEquals("field13", values.get(12).getKey());
		Assert.assertNull(values.get(12).getValue());
		Assert.assertEquals("field14", values.get(13).getKey());
		Assert.assertNull(values.get(13).getValue());
		Assert.assertEquals("field15", values.get(14).getKey());
		Assert.assertNull(values.get(14).getValue());
		Assert.assertEquals("field16", values.get(15).getKey());
		Assert.assertNull(values.get(15).getValue());
		Assert.assertEquals("field17", values.get(16).getKey());
		Assert.assertEquals("value18", values.get(17).getValue());
		Assert.assertEquals("field18", values.get(17).getKey());
		Assert.assertEquals("value19", values.get(18).getValue());
		Assert.assertEquals("field19", values.get(18).getKey());
		Assert.assertEquals("field20", values.get(19).getKey());
		Assert.assertEquals("value20", values.get(19).getValue());
	}

}
