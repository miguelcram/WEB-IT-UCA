package es.uca.iw.webituca.Service;

import es.uca.iw.webituca.Model.Cartera;
import es.uca.iw.webituca.Repository.CarteraRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Service
public class CarteraService {

    @Autowired
    private CarteraRepository carteraRepository;

    public List<Cartera> getAllCarteras() {
        return carteraRepository.findAll();
    }

    public Cartera getCarteraById(Long id) {
        Optional<Cartera> cartera = carteraRepository.findById(id);
        return cartera.orElse(null);
    }

    public Cartera createCartera(Cartera cartera) {
        return carteraRepository.save(cartera);
    }

    public Cartera updateCartera(Long id, Cartera cartera) {
        if (carteraRepository.existsById(id)) {
            cartera.setId(id);
            return carteraRepository.save(cartera);
        }
        return null;
    }

    public void deleteCartera(Long id) {
        carteraRepository.deleteById(id);
    }
    public Optional<Cartera> getCarteraActual() {
        LocalDateTime now = LocalDateTime.now();
        return carteraRepository.findFirstByFechaCreacionLessThanEqualAndFechaFinGreaterThanEqual(now, now);
    }
}
