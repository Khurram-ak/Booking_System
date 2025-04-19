package test;

import main.BookingSystem;
import model.Patient;
import model.Physiotherapist;
import model.Treatment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BookingSystemTests {

	private BookingSystem system;
	private Physiotherapist physio;
	private Patient patient;

	@BeforeEach
	public void setUp() {
		system = new BookingSystem();
		physio = new Physiotherapist(1, "Dr. Test", "123 Test St", "1234567890", Arrays.asList("Physiotherapy"));
		patient = new Patient(1, "Test Patient", "456 Patient Rd", "0987654321");
		system.addPhysiotherapist(physio);
		system.addPatient(patient);
	}

	@Test
	public void testAddAndRemovePatient() {
		assertEquals(1, system.getAllPatients().size());
		system.removePatient(1);
		assertEquals(0, system.getAllPatients().size());
	}

	@Test
	public void testAddAndRemovePhysiotherapist() {
		assertEquals(1, system.getAllPhysiotherapists().size());
		system.removePhysiotherapist(1);
		assertEquals(0, system.getAllPhysiotherapists().size());
	}

	@Test
	public void testSearchPhysiotherapistByExpertise() {
		List<Physiotherapist> found = system.searchPhysiotherapistsByExpertise("Physiotherapy");
		assertEquals(1, found.size());
		assertEquals("Dr. Test", found.get(0).getName());
	}

	@Test
	public void testSearchPhysiotherapistByName() {
		assertTrue(system.searchPhysiotherapistByName("Dr. Test").isPresent());
		assertFalse(system.searchPhysiotherapistByName("Unknown").isPresent());
	}

	@Test
	public void testBookAppointment() {
		LocalDateTime dateTime = LocalDateTime.now().plusDays(1);
		Treatment treatment = new Treatment("Test Treatment", dateTime, physio);
		physio.addTreatment(treatment);

		boolean booked = system.bookAppointment(patient, treatment);
		assertTrue(booked);
		assertEquals("Booked", treatment.getStatus());
		assertEquals(patient, treatment.getPatient());
	}

	@Test
	public void testDoubleBookingConflict() {
		LocalDateTime dateTime = LocalDateTime.now().plusDays(1);
		Treatment t1 = new Treatment("T1", dateTime, physio);
		physio.addTreatment(t1);
		system.bookAppointment(patient, t1);

		Treatment t2 = new Treatment("T2", dateTime, physio);
		physio.addTreatment(t2);
		boolean booked = system.bookAppointment(patient, t2);
		assertFalse(booked);
	}

	@Test
	public void testCancelAppointment() {
		LocalDateTime dateTime = LocalDateTime.now().plusDays(1);
		Treatment treatment = new Treatment("Cancel Test", dateTime, physio);
		physio.addTreatment(treatment);
		system.bookAppointment(patient, treatment);

		boolean cancelled = system.cancelAppointment(treatment);
		assertTrue(cancelled);
		assertEquals("Cancelled", treatment.getStatus());
		assertNull(treatment.getPatient());
	}

	@Test
	public void testAttendAppointment() {
		LocalDateTime dateTime = LocalDateTime.now().plusDays(1);
		Treatment treatment = new Treatment("Attend Test", dateTime, physio);
		physio.addTreatment(treatment);
		system.bookAppointment(patient, treatment);

		boolean attended = system.attendAppointment(treatment);
		assertTrue(attended);
		assertEquals("Attended", treatment.getStatus());
	}

	@Test
	public void testChangeAppointment() {
		LocalDateTime dt1 = LocalDateTime.now().plusDays(1);
		LocalDateTime dt2 = LocalDateTime.now().plusDays(2);
		Treatment oldT = new Treatment("Old", dt1, physio);
		Treatment newT = new Treatment("New", dt2, physio);
		physio.addTreatment(oldT);
		physio.addTreatment(newT);

		system.bookAppointment(patient, oldT);
		boolean changed = system.changeAppointment(patient, oldT, newT);
		assertTrue(changed);
		assertEquals("Cancelled", oldT.getStatus());
		assertEquals("Booked", newT.getStatus());
		assertEquals(patient, newT.getPatient());
	}

	@Test
	public void testGetAvailableTreatments() {
		Treatment t1 = new Treatment("Available 1", LocalDateTime.now().plusDays(1), physio);
		Treatment t2 = new Treatment("Available 2", LocalDateTime.now().plusDays(2), physio);
		physio.addTreatment(t1);
		physio.addTreatment(t2);

		system.bookAppointment(patient, t1);

		List<Treatment> available = system.getAvailableTreatments(physio);
		assertEquals(1, available.size());
		assertEquals("Available 2", available.get(0).getName());
	}
}
