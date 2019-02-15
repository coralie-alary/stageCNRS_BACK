//INTERFACE DAO

package fr.eccad.eccad2.datasets;

import java.util.List;
import java.util.Map;

import fr.eccad.models.dto.category.CategoryDTO;
import fr.eccad.models.dto.category.ParametreDTO;
import fr.eccad.models.dto.datasets.CategoryGroupDatasetDTO;
import fr.eccad.models.dto.datasets.InventoryDatasetDTO;
import fr.eccad.models.dto.datasets.InventoryGroupDatasetDTO;
import fr.eccad.models.dto.datasets.InventoryScenarioDatasetDTO;
import fr.eccad.models.dto.datasets.InventorySectorDatasetDTO;
import fr.eccad.models.dto.datasets.PublicationDTO;
import fr.eccad.models.dto.inventory.InventoryDTO;
import fr.eccad.models.dto.inventory.ScenarioDTO;
import fr.eccad.models.dto.netcdf.GridDTO;
import fr.eccad.models.dto.person.OrganizationDTO;
import fr.eccad.models.dto.person.PersonDTO;
import fr.eccad.models.dto.sector.SectorDTO;

public interface DatasetService {

	List<CategoryGroupDatasetDTO> findAllCategoryGroups();

	List<InventoryGroupDatasetDTO> getDataSetsInventory(String nameCatGoup);

	List<InventoryDatasetDTO> getMetadataByInventorygroupId(Long idInventoryGroup);

	List<InventoryDatasetDTO> getMetadataByInventoryName(String name);

	List<InventoryDatasetDTO> getMetadataByInventoryId(Long idInventory);

	List<InventoryDatasetDTO> getMetadataByCategoryGroupId(Long idCategoryGroup);

	Map<Long, List<ParametreDTO>> findParameters();

	List<CategoryDTO> findCategoriesByCategoryGroupName(String categoryGroupName);

	List<SectorDTO> findSectorsByInventoryId(Long inventoryId);

	Map<String, List<GridDTO>> emissionsTimeSeries(Long parameterId, Long categoryId, int isGlobaRegionalOrWithMask,
			String regionMask, String email);

	Map<String, List<GridDTO>> inventoryParameterTimeSeries(Long inventoryId, Long categoryId, Long scenarioId);

	List<InventoryDTO> findInventoriesFromCategoryId(Long categoryId, String email);

	List<ParametreDTO> findParametersFromCategoryIdAndEmail(Long categoryId, String email);

	String generateCsv(String csvFileName, Map<String, List<GridDTO>> gridData);

	List<InventoryDTO> getInventoriesWithSectorsAndByCategoryId(Long categoryId);

	String createMetadataPdf(InventoryDatasetDTO inventoryDatasetDTO);

	List<ScenarioDTO> findScenarioByInventoryId(Long inventoryId);

	List<InventoryDTO> findInventoriesWithScenarios();

	List<InventoryScenarioDatasetDTO> findInventoriesAndScenarios();

	List<InventorySectorDatasetDTO> findInventoriesAndSectors();

	InventoryDTO findInventoryByName(String inventoryName);

	List<PublicationDTO> findPublicationsByInventoryId(Long inventoryId);

	List<InventoryGroupDatasetDTO> getDataSetsInventoryFromServer(String nameCatGoup);

// -------------------------------------------------------------------------------------
	List<OrganizationDTO> findAllOrganizations();

	// j'ajoute la méthode pour trouver le nbre de pers par organisation =
	// JE DEFINIE LA METHODE
	int findAllPersonsForOrganizations(Long organizationId);

	List<PersonDTO> findAllPersons();
}
