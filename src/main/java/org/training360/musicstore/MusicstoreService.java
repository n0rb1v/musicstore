package org.training360.musicstore;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Service
public class MusicstoreService {
    private ModelMapper modelMapper;
    private AtomicLong idgen = new AtomicLong();
//    private List<Instrument> instruments = new ArrayList<>();
    private List<Instrument> instruments = new ArrayList<>(List.of(
            new Instrument(1,"Fender", InstrumentType.ELECTRIC_GUITAR, 2000,LocalDate.now()),
            new Instrument(2,"Gibson", InstrumentType.ELECTRIC_GUITAR, 3000,LocalDate.now())
    ));

    public MusicstoreService(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<InstrumentDTO> listAll(Optional<String> search, Optional<Integer> price) {
        Type trgListType = new TypeToken<List<InstrumentDTO>>() {
        }.getType();
        List<Instrument> filter = instruments.stream()
                .filter(i -> search.isEmpty() || i.getBrand().contains(search.get()))
                .filter(i -> price.isEmpty() || i.getPrice() == price.get())
                .collect(Collectors.toList());
        return modelMapper.map(filter, trgListType);
    }

    public InstrumentDTO createInstrument(CreateInstrumentCommand command) {
        Instrument instrument = new Instrument(
                idgen.incrementAndGet(), command.getBrand(), command.getInstrumentType(), command.getPrice(), LocalDate.now());
        instruments.add(instrument);
        return modelMapper.map(instrument, InstrumentDTO.class);

    }

    public InstrumentDTO findInstrumentById(long id) {
        return modelMapper.map(instruments.stream()
                .filter(i -> i.getId() == id)
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Not found: " + id)), InstrumentDTO.class);
    }

    public InstrumentDTO updateInstrument(long id, UpdatePriceCommand command) {
        Instrument instrument = instruments.stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found: " + id));
        if (command.getPrice() != instrument.getPrice()) {
            instrument.setPrice(command.getPrice());
            instrument.setPostDate(LocalDate.now());
        }
        return modelMapper.map(instrument, InstrumentDTO.class);
    }

    public void deleteInstrument(long id) {
        Instrument instrument = instruments.stream()
                .filter(i -> i.getId() == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Not found: " + id));
        instruments.remove(instrument);
    }

    public void deleteAll() {
        idgen = new AtomicLong();
        instruments.clear();
    }
}
