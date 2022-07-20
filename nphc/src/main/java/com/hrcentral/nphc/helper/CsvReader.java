package com.hrcentral.nphc.helper;

import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

public class CsvReader {


	public static <T> List<T> toList(Class<T> Entity, byte[] data) {

		CsvMapper csvMapper = new CsvMapper();
		List<T> resulttList = new ArrayList<>();
		
		CsvSchema csvSchema = csvMapper.typedSchemaFor(Entity).withHeader();
		
		try {
			MappingIterator<T> objectList  = csvMapper.readerWithSchemaFor(Entity).with(csvSchema).readValues(data);
			resulttList = objectList.readAll();
		} catch (IOException ex) {
			if (ex.getMessage().toLowerCase().contains("too many entries")) {
				throw new CustomException(ResponseMessage.MSG_ERR_CSV_COLUMN_FORMAT);
			}
		}
		return resulttList;
	}
}
