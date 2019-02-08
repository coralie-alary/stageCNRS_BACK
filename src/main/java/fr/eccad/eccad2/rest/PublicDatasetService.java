//CONTROLER

package fr.eccad.eccad2.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.eccad.eccad2.datasets.DatasetServiceImpl;
import fr.eccad.models.dto.datasets.CategoryGroupDatasetDTO;
import fr.eccad.models.dto.datasets.InventoryScenarioDatasetDTO;
import fr.eccad.models.dto.datasets.InventorySectorDatasetDTO;
import fr.eccad.models.dto.person.OrganizationDTO;

@RestController
@RequestMapping("/public/datasets")
public class PublicDatasetService {
	@Autowired
	private DatasetServiceImpl dao;

	@GetMapping("isAlive")
	public ResponseEntity isAlive() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("response", "response");
		return new ResponseEntity("yes i'am alive, HAVE A NICE DAY !", httpHeaders, HttpStatus.OK);
	}

	@GetMapping("inventoriesandscenarios")
	public List<InventoryScenarioDatasetDTO> findInventoriesAndScenarios() {
		return dao.findInventoriesAndScenarios();
	}

	// autres exemples
	@GetMapping("InventorySectorDatasetDTO")
	public List<InventorySectorDatasetDTO> findInventoriesAndSectors() {
		return dao.findInventoriesAndSectors();
	}

	// autres exemples
	@GetMapping("CategoryGroups")
	public List<CategoryGroupDatasetDTO> findAllCategoryGroups() {
		return dao.findAllCategoryGroups();
	}

	// Récupérer la liste des organizations
	@GetMapping("Organization")
	public List<OrganizationDTO> findAllOrganizations() {
		// je récupère avec le DAO la liste de toutes les organizations
		List<OrganizationDTO> allOrganizations = dao.findAllOrganizations();

		// pour chaque Organization
		for (OrganizationDTO organizationDTO : allOrganizations) {
			// je récupère l'ID de l'organization
			long id = organizationDTO.getIdOrganization();
			// je vais trouver le nombre de personnes pour l'organization dont je passe l'id
			int nbPersons = dao.findAllPersonsForOrganizations(id);
			// je mets le nombre de personnes dans l'objet organization
			organizationDTO.setNumberOfPersons(nbPersons);
		}
		return allOrganizations;
	}

}
