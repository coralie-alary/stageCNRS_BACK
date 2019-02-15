
// cette classe communique avec la BDD pour récupérer les informations ou en ajouter

package fr.eccad.eccad2.datasets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.eccad.models.constant.EccadConstants;
import fr.eccad.models.dto.DtoTools;
import fr.eccad.models.dto.category.CategoryDTO;
import fr.eccad.models.dto.category.ParametreDTO;
import fr.eccad.models.dto.datasets.CategoryDataset;
import fr.eccad.models.dto.datasets.CategoryGroupDatasetDTO;
import fr.eccad.models.dto.datasets.InventoryCategoryDataset;
import fr.eccad.models.dto.datasets.InventoryDatasetDTO;
import fr.eccad.models.dto.datasets.InventoryGroupDatasetDTO;
import fr.eccad.models.dto.datasets.InventoryScenarioDatasetDTO;
import fr.eccad.models.dto.datasets.InventorySectorDatasetDTO;
import fr.eccad.models.dto.datasets.PersonDataset;
import fr.eccad.models.dto.datasets.PublicationDTO;
import fr.eccad.models.dto.inventory.InventoryDTO;
import fr.eccad.models.dto.inventory.ScenarioDTO;
import fr.eccad.models.dto.netcdf.GridDTO;
import fr.eccad.models.dto.person.OrganizationDTO;
import fr.eccad.models.dto.person.PersonDTO;
import fr.eccad.models.dto.sector.SectorDTO;
import fr.eccad.models.dto.tools.OrderComparator;
import fr.sedoo.eccad2.api.dao.DAOException;
import fr.sedoo.eccad2.api.dao.category.CategoryDAO;
import fr.sedoo.eccad2.api.dao.category.CategoryGroupDAO;
import fr.sedoo.eccad2.api.dao.category.ParametreDAO;
import fr.sedoo.eccad2.api.dao.category.ParametreGroupDAO;
import fr.sedoo.eccad2.api.dao.geography.LegendDAO;
import fr.sedoo.eccad2.api.dao.inventory.IntervalDAO;
import fr.sedoo.eccad2.api.dao.inventory.InventoryCategoryDAO;
import fr.sedoo.eccad2.api.dao.inventory.InventoryDAO;
import fr.sedoo.eccad2.api.dao.inventory.InventoryGroupDAO;
import fr.sedoo.eccad2.api.dao.inventory.InventorySectorDAO;
import fr.sedoo.eccad2.api.dao.inventory.PublicationDAO;
import fr.sedoo.eccad2.api.dao.inventory.RestrictedDAO;
import fr.sedoo.eccad2.api.dao.inventory.ScenarioDAO;
import fr.sedoo.eccad2.api.dao.netCDF.GeospatialDAO;
import fr.sedoo.eccad2.api.dao.netCDF.GridDAO;
import fr.sedoo.eccad2.api.dao.netCDF.NetcdfDAO;
import fr.sedoo.eccad2.api.dao.netCDF.ResolutionDAO;
import fr.sedoo.eccad2.api.dao.person.OrganizationDAO;
import fr.sedoo.eccad2.api.dao.person.PersonDAO;
import fr.sedoo.eccad2.api.dao.person.ProjectDAO;
import fr.sedoo.eccad2.api.dao.person.ProviderDAO;
import fr.sedoo.eccad2.api.dao.sector.SectorDAO;
import fr.sedoo.eccad2.api.dao.update.UpdateHistoryDAO;
import fr.sedoo.eccad2.api.modele.category.Category;
import fr.sedoo.eccad2.api.modele.category.CategoryGroup;
import fr.sedoo.eccad2.api.modele.category.Parametre;
import fr.sedoo.eccad2.api.modele.category.ParametreGroup;
import fr.sedoo.eccad2.api.modele.geography.Legend;
import fr.sedoo.eccad2.api.modele.inventory.Interval;
import fr.sedoo.eccad2.api.modele.inventory.Inventory;
import fr.sedoo.eccad2.api.modele.inventory.InventoryCategory;
import fr.sedoo.eccad2.api.modele.inventory.InventoryGroup;
import fr.sedoo.eccad2.api.modele.inventory.InventorySector;
import fr.sedoo.eccad2.api.modele.inventory.Publication;
import fr.sedoo.eccad2.api.modele.inventory.Restricted;
import fr.sedoo.eccad2.api.modele.inventory.Scenario;
import fr.sedoo.eccad2.api.modele.netCDF.Geospatial;
import fr.sedoo.eccad2.api.modele.netCDF.Netcdf;
import fr.sedoo.eccad2.api.modele.netCDF.Resolution;
import fr.sedoo.eccad2.api.modele.person.Organization;
import fr.sedoo.eccad2.api.modele.person.Person;
import fr.sedoo.eccad2.api.modele.person.Project;
import fr.sedoo.eccad2.api.modele.person.Provider;
import fr.sedoo.eccad2.api.modele.sector.Sector;
import fr.sedoo.eccad2.api.modele.update.UpdateHistory;

@Service
public class DatasetServiceImpl implements DatasetService {

	@Autowired
	private CategoryGroupDAO categoryGroupDAO;
	@Autowired
	private DtoTools dtoTools;
	@Autowired
	private InventoryGroupDAO inventoryGroupDAO;
	@Autowired
	private InventoryDAO inventoryDAO;
	@Autowired
	private GeospatialDAO geospatialDAO;
	@Autowired
	private InventoryCategoryDAO inventoryCategoryDAO;
	@Autowired
	private CategoryDAO categoryDAO;
	@Autowired
	private IntervalDAO intervalDAO;
	@Autowired
	private ParametreDAO parametreDAO;
	@Autowired
	private ResolutionDAO resolutionDAO;
	@Autowired
	private OrganizationDAO organizationDAO;
	@Autowired
	private ProviderDAO providerDAO;
	@Autowired
	private ProjectDAO projectDAO;
	@Autowired
	private UpdateHistoryDAO updateHistoryDAO;
	@Autowired
	private NetcdfDAO netcdfDAO;
	@Autowired
	private LegendDAO legendDAO;
	@Autowired
	private SectorDAO sectorDAO;
	@Autowired
	private PersonDAO personDAO;
	@Autowired
	private ScenarioDAO scenarioDAO;
	@Autowired
	private PublicationDAO publicationDAO;
	@Autowired
	private ParametreGroupDAO parametreGroupDAO;
	@Autowired
	private InventorySectorDAO inventorySectorDAO;
	@Autowired
	private RestrictedDAO restrictedDAO;
	@Autowired
	private GridDAO gridDAO;
	private Map<String, List<InventoryGroupDatasetDTO>> inventoryGroupCache = new HashMap<>();
	private Date lastEccadUpdate;

	@Override
	public ArrayList<CategoryGroupDatasetDTO> findAllCategoryGroups() {

		ArrayList<CategoryGroupDatasetDTO> result = new ArrayList<CategoryGroupDatasetDTO>();
		try {
			Set<CategoryGroup> categoryGroups = categoryGroupDAO.findAll();

			for (CategoryGroup currentCatGroup : categoryGroups) {
				CategoryGroupDatasetDTO ctgDto = new CategoryGroupDatasetDTO();
				ctgDto = dtoTools.toCategoryGroupDatasetsDTO(currentCatGroup);
				result.add(ctgDto);
			}

		} catch (DAOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<InventoryGroupDatasetDTO> getDataSetsInventoryFromServer(String nameCatGoup) {
		List<InventoryGroupDatasetDTO> results = new ArrayList<InventoryGroupDatasetDTO>();

		try {
			CategoryGroup categoryGoup = categoryGroupDAO.findByName(nameCatGoup.toLowerCase().trim(), true);

			List<InventoryGroup> inventoryGroups = inventoryGroupDAO.findByCategoryGroupId(categoryGoup.getId());

			for (InventoryGroup currentGroup : inventoryGroups) {

				List<Inventory> inventories = inventoryDAO.findInventoriesByInventoryGroupId(currentGroup.getId());

				List<Geospatial> geospatialList = new ArrayList<Geospatial>();

				if (!inventories.get(0).isIsglobalInventory()) {
					geospatialList = geospatialDAO.findByInventoryGroupId(currentGroup.getId());
				}

				List<InventoryCategory> invCategories = inventoryCategoryDAO
						.findByInventoryGroupId(currentGroup.getId());

				Set<Category> categories = new HashSet<Category>();

				Set<InventoryCategoryDataset> invCatReloaded = new HashSet<InventoryCategoryDataset>();

				for (InventoryCategory currentInvCat : invCategories) {
					InventoryCategoryDataset invCat = new InventoryCategoryDataset();
					Interval interval = intervalDAO.findByInventoryIdAndCategoryId(currentInvCat.getInventory().getId(),
							currentInvCat.getCategory().getId());

					invCat.setId(currentInvCat.getId()).setIdInvCat(currentInvCat.getIdInvCat())
							.setInventory(currentInvCat.getInventory()).setCategory(currentInvCat.getCategory())
							.setInterval(interval).setStartDateInvCat(currentInvCat.getStartDateInvCat())
							.setEndDateInvCat(currentInvCat.getEndDateInvCat())
							.setVariableNameSum(currentInvCat.getVariableNameSum()).setSector(currentInvCat.isSector());

					categories.add(currentInvCat.getCategory());
					invCatReloaded.add(invCat);
				}

				Date publication = getFirstPublicationDate(inventories);

				ArrayList<CategoryDataset> categoryList = new ArrayList<>();
				for (Category currentCat : categories) {
					CategoryDataset cat = new CategoryDataset();
					cat.setId(currentCat.getId()).setIdCategory(currentCat.getIdCategory())
							.setCategoryGroup(currentCat.getCategoryGroup())
							.setColorPalette(currentCat.getColorPalette())
							.setFullnameCategory(currentCat.getFullnameCategory())
							.setShortNameCategory(currentCat.getShortNameCategory())
							.setOrderCategory(currentCat.getOrderCategory())
							.setColorCategory(currentCat.getColorCategory())
							.setDisplayTitleCategory(currentCat.getDisplayTitleCategory())
							.setDisplayTitleSectorCategory(currentCat.getDisplayTitleSectorCategory())
							.setMask(currentCat.isMask()).setSectors(currentCat.getSectors());

					if (cat.getCategoryGroup().equals(categoryGoup)) {
						Set<InventoryCategoryDataset> invCats = new HashSet<InventoryCategoryDataset>();
						for (InventoryCategoryDataset currentInvCat : invCatReloaded) {
							if (cat.getId().equals(currentInvCat.getCategory().getId())) {
								invCats.add(currentInvCat);
							}
						}
						List<Parametre> parametres = parametreDAO
								.findByCategoryIdAndInventoryGroupId(currentCat.getId(), currentGroup.getId());
						cat.setParametres(new HashSet<Parametre>(parametres));
						cat.setInventoryCategories(invCats);
						categoryList.add(cat);
					}
				}

				List<Resolution> resolutions = resolutionDAO.findByInventoryGroupId(currentGroup.getId());

				List<Organization> organizations = organizationDAO.findByInventoryGroupId(currentGroup.getId());

				Project project = null;
				try {

					List<Provider> providers = providerDAO.findByInventoryId(inventories.get(0).getId(), true);
					for (Provider provider : providers) {
						Provider p = providerDAO.findByID(provider.getId(), true);
						if (p.getProject() != null) {
							project = p.getProject();
							break;
						}
					}
				} catch (Exception e) {

				}
				results.add(dtoTools.toInventoryGroupDatasetDTO(categoryGoup, currentGroup, publication, categoryList,
						inventories.get(0), resolutions, organizations, geospatialList, project));

			}
			Collections.sort(results, new OrderComparator());

		} catch (DAOException e) {
			e.printStackTrace();
		}

		inventoryGroupCache.put(nameCatGoup, results);

		return results;
	}

	private Date getLastEccadUpdate() {

		try {
			UpdateHistory updateHistory = updateHistoryDAO.getLastUpdate();
			if (updateHistory != null) {
				return updateHistory.getDateUpdateHistory();
			}
		} catch (DAOException e) {
		}

		return null;
	}

	@Override
	public List<InventoryGroupDatasetDTO> getDataSetsInventory(String nameCatGoup) {

		boolean getFromcatche = false;

		if (this.lastEccadUpdate == null) {
			this.lastEccadUpdate = this.getLastEccadUpdate();
		} else {
			Date lastEccadUpdateFromServer = this.getLastEccadUpdate();
			if (lastEccadUpdateFromServer.equals(this.lastEccadUpdate)) {
				getFromcatche = true;
			} else {
				this.lastEccadUpdate = lastEccadUpdateFromServer;
				inventoryGroupCache = new HashMap<>();
			}
		}

		if (getFromcatche) {
			List<InventoryGroupDatasetDTO> result = this.inventoryGroupCache.get(nameCatGoup);
			if (result != null) {
				return result;
			}
		}

		return getDataSetsInventoryFromServer(nameCatGoup);

	}

	public Date getFirstPublicationDate(List<Inventory> inventories) {
		Date result = new Date();
		for (Inventory currentInventory : inventories) {
			if (result.compareTo(currentInventory.getPubdateInventory()) == 1) {
				result = currentInventory.getPubdateInventory();
			}
		}
		return result;
	}

	@Override
	public ArrayList<InventoryDatasetDTO> getMetadataByInventorygroupId(Long idInventoryGroup) {

		Set<Inventory> inventories = null;

		try {
			InventoryGroup group = inventoryGroupDAO.findByID(idInventoryGroup, true);

			inventories = group.getInventories();
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return getMetadataByInventoryList(new ArrayList<>(inventories));
	}

	@Override
	public ArrayList<InventoryDatasetDTO> getMetadataByInventoryId(Long idInventory) {

		ArrayList<Inventory> inventories = new ArrayList<Inventory>();

		try {
			Inventory inventory = inventoryDAO.findByID(idInventory, true);

			inventories.add(inventory);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return getMetadataByInventoryList(inventories);
	}

	@Override
	public ArrayList<InventoryDatasetDTO> getMetadataByInventoryName(String inventoryName) {

		ArrayList<Inventory> result = new ArrayList<Inventory>();

		try {

			List<Inventory> tmp = inventoryDAO.findInventoryByInventoryGroupTitle(inventoryName);

			result.addAll(tmp);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return getMetadataByInventoryList(result);
	}

	public ArrayList<InventoryDatasetDTO> getMetadataByInventoryList(List<Inventory> rawInventories) {

		ArrayList<InventoryDatasetDTO> result = new ArrayList<InventoryDatasetDTO>();
		List<Inventory> inventories = rawInventories;

		try {

			for (Inventory currentInventory : inventories) {
				Inventory inventory = currentInventory;

				inventory = inventoryDAO.findByID(currentInventory.getId(), true);

				List<InventoryCategory> inventoryCategories = inventoryCategoryDAO
						.findByInventoryId(currentInventory.getIdInventory());

				Set<InventoryCategoryDataset> inventoryCategoriesReloaded = new HashSet<InventoryCategoryDataset>();

				Map<String, List<Legend>> regions = new HashMap<String, List<Legend>>();
				for (InventoryCategory currentInvCat : inventoryCategories) {
					InventoryCategoryDataset invCat = new InventoryCategoryDataset();
					Interval interval = intervalDAO.findByInventoryIdAndCategoryId(currentInvCat.getInventory().getId(),
							currentInvCat.getCategory().getId());

					invCat.setId(currentInvCat.getId()).setIdInvCat(currentInvCat.getIdInvCat())
							.setInventory(currentInvCat.getInventory()).setCategory(currentInvCat.getCategory())
							.setInterval(interval).setStartDateInvCat(currentInvCat.getStartDateInvCat())
							.setEndDateInvCat(currentInvCat.getEndDateInvCat())
							.setVariableNameSum(currentInvCat.getVariableNameSum()).setSector(currentInvCat.isSector());

					inventoryCategoriesReloaded.add(invCat);
				}

				List<Netcdf> netcdfs = netcdfDAO.findListNetcdfByInventoryCategory(inventoryCategories.get(0).getId(),
						true);

				for (Netcdf netcdf : netcdfs) {
					List<Legend> legends = new ArrayList<Legend>();
					try {
						Set<Legend> aux = legendDAO.findByNetCdfId(String.valueOf(netcdf.getId()));
						for (Legend l : aux) {
							legends.add(l);
						}
						Collections.sort(legends, new Comparator<Legend>() {
							@Override
							public int compare(final Legend object1, final Legend object2) {
								return object1.getOrderLegend() - object2.getOrderLegend();
							}
						});
					} catch (Exception e) {
					}
					regions.put(netcdf.getParametre().getDisplayNameParametre(), legends);
				}

				List<Category> categories = categoryDAO.findByInventoryId(currentInventory.getIdInventory());

				List<CategoryDataset> categoriesReloaded = new ArrayList<CategoryDataset>();

				for (Category currentCategory : categories) {
					CategoryDataset cat = new CategoryDataset();
					cat.setId(currentCategory.getId()).setIdCategory(currentCategory.getIdCategory())
							.setCategoryGroup(currentCategory.getCategoryGroup())
							.setColorPalette(currentCategory.getColorPalette())
							.setFullnameCategory(currentCategory.getFullnameCategory())
							.setShortNameCategory(currentCategory.getShortNameCategory())
							.setOrderCategory(currentCategory.getOrderCategory())
							.setColorCategory(currentCategory.getColorCategory())
							.setDisplayTitleCategory(currentCategory.getDisplayTitleCategory())
							.setDisplayTitleSectorCategory(currentCategory.getDisplayTitleSectorCategory())
							.setMask(currentCategory.isMask()).setSectors(currentCategory.getSectors());

					List<Sector> sectors = sectorDAO.findByIdCategoryAndIdInventory(cat.getIdCategory(),
							currentInventory.getIdInventory());
					cat.setSectors(new HashSet<>(sectors));

					List<Parametre> params = parametreDAO.findByCategoryIdAndInventoryId(cat.getIdCategory(),
							currentInventory.getIdInventory());
					cat.setParametres(new HashSet<>(params));

					categoriesReloaded.add(cat);
				}

				List<Resolution> resolutions = resolutionDAO.findByInventoryId(currentInventory.getIdInventory());

				List<Geospatial> geospatials = geospatialDAO.findByInventoryId(currentInventory.getIdInventory());

				List<Person> persons = personDAO.findByInventoryIdAndIsProvider(currentInventory.getIdInventory());

				List<PersonDataset> personsReloaded = new ArrayList<PersonDataset>();

				List<Scenario> scenarios = null;
				try {
					scenarios = scenarioDAO.findScenarioByInventoryrId(inventories.get(0).getId());
				} catch (Exception e) {

				}

				for (Person currentPerson : persons) {
					Organization orga = organizationDAO.findByID(currentPerson.getOrganization().getIdOrganization(),
							true);
					PersonDataset person = new PersonDataset();
					person.setIdPerson(currentPerson.getId());
					person.setEmailPerson(currentPerson.getEmailPerson());
					person.setFirstNamePerson(currentPerson.getFirstNamePerson());
					person.setLastNamePerson(currentPerson.getLastNamePerson());
					person.setOrganization(orga);

					personsReloaded.add(person);
				}

				List<Publication> publications = publicationDAO.findByInventoryId(currentInventory.getIdInventory());

				result.add(dtoTools.toInventoryDatasetsDTO(inventory, inventoryCategoriesReloaded, categoriesReloaded,
						resolutions, geospatials, personsReloaded, publications, scenarios, regions));
			}

		} catch (DAOException e) {
			e.printStackTrace();
		}

		return result;

	}

	@Override
	public ArrayList<InventoryDatasetDTO> getMetadataByCategoryGroupId(Long categoryGroupId) {

		ArrayList<InventoryDatasetDTO> result = new ArrayList<InventoryDatasetDTO>();
		Set<Inventory> inventories = new HashSet<Inventory>();
		return result;
	}

	@Override
	public Map<Long, List<ParametreDTO>> findParameters() {

		Map<Long, List<ParametreDTO>> result = new HashMap<Long, List<ParametreDTO>>();
		try {
			List<ParametreGroup> parametreGroups = parametreGroupDAO.findToClean();

			for (ParametreGroup p : parametreGroups) {
				Long parametreGroupId = p.getIdParametreGroup();
				String parameterGroupName = p.getFullnameParametreGroup();
				ArrayList<ParametreDTO> aux = new ArrayList<ParametreDTO>();
				result.put(parametreGroupId, aux);

				List<ParametreGroup> resultGroup = new ArrayList<ParametreGroup>();
				resultGroup.add(p);
				List<ParametreGroup> params = parametreGroupDAO.findByGroup(parametreGroupId.toString());

				resultGroup.addAll(params);

				if (resultGroup.size() > 0) {
					for (ParametreGroup pargr : resultGroup) {
						if (!pargr.getFullnameParametreGroup().contains("None")) {

							List<Parametre> parametres = parametreDAO.findByParameterGroupId(pargr.getId());
							List<ParametreDTO> tmp = result.get(parametreGroupId);

							for (Parametre para : parametres) {

								ParametreDTO parameterDTO = dtoTools.toParameterDTO(para, true);
								parameterDTO.setParameterGroupName(parameterGroupName);
								tmp.add(parameterDTO);
							}
						}
					}
				}
			}
		} catch (Exception e) {

		}
		return result;
	}

	@Override
	public List<CategoryDTO> findCategoriesByCategoryGroupName(String categoryGroupName) {
		ArrayList<CategoryDTO> result = new ArrayList<CategoryDTO>();
		try {
			CategoryGroup categoryGroup = categoryGroupDAO.findByName(categoryGroupName, true);
			List<Category> list = categoryDAO.findByCategoryGroup(categoryGroup.getId());
			for (Category category : list) {
				boolean b = false;
				for (CategoryDTO c : result) {
					if (c.getId() == category.getId()) {
						b = true;
						break;
					}
				}
				if (!b) {
					CategoryDTO categoryDTO = dtoTools.toCategoryDTO(category, true);
					result.add(categoryDTO);
				}

			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public ArrayList<SectorDTO> findSectorsByInventoryId(Long inventoryId) {
		ArrayList<SectorDTO> result = new ArrayList<SectorDTO>();
		try {
			List<InventorySector> sectors = inventorySectorDAO.findInventorySectorByInventoryId(inventoryId);
			for (InventorySector invSector : sectors) {
				SectorDTO s = dtoTools.toSectorDTO(invSector.getSector(), true);
				result.add(s);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		Collections.sort(result, new Comparator<SectorDTO>() {
			@Override
			public int compare(final SectorDTO object1, final SectorDTO object2) {
				return object1.getOrder() - object2.getOrder();
			}
		});

		return result;
	}

	@Override
	public Map<String, List<GridDTO>> emissionsTimeSeries(Long parameterId, Long categoryId,
			int isGlobaRegionalOrWithMask, String regionMask, String email) {

		Map<String, List<GridDTO>> timeSeries = new HashMap<String, List<GridDTO>>();
		return timeSeries;

	}

	private List<Inventory> getInventoryList(long parameterId, int isGlobaRegionalOrWithMask) {
		List<Inventory> inventoryList = new ArrayList<Inventory>();
		try {
			if (isGlobaRegionalOrWithMask == EccadConstants.WITHMASK_INDEX) {
				inventoryList.addAll(inventoryDAO.findInventoriesByParametreIdAndIsGlobal(parameterId, true));
				inventoryList.addAll(inventoryDAO.findInventoriesByParametreIdAndIsGlobal(parameterId, false));
			} else {
				boolean globalOrRegional = false;
				if (isGlobaRegionalOrWithMask == EccadConstants.GLOBAL_INDEX) {
					globalOrRegional = true;
				}

				inventoryList
						.addAll(inventoryDAO.findInventoriesByParametreIdAndIsGlobal(parameterId, globalOrRegional));
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}

		return inventoryList;
	}

	@Override
	public Map<String, List<GridDTO>> inventoryParameterTimeSeries(Long inventoryId, Long categoryId, Long scenarioId) {
		Map<String, List<GridDTO>> timeSeries = new HashMap<String, List<GridDTO>>();
		return timeSeries;
	}

	@Override
	public List<InventoryDTO> findInventoriesFromCategoryId(Long categoryId, String email) {
		List<InventoryDTO> result = new ArrayList<InventoryDTO>();

		try {
			long idPerson = -1;
			if (email != null && email.length() > 0) {
				idPerson = personDAO.findByEmail(email, true).getIdPerson();
			}

			List<InventoryCategory> list = inventoryCategoryDAO.findByCategoryId(categoryId);
			for (InventoryCategory invCat : list) {
				InventoryCategory iC = inventoryCategoryDAO.findByID(invCat.getId(), true);
				if (!iC.getInventory().isVisualisationRestrictedInvent()) {
					result.add(dtoTools.toInventoryDTO(iC.getInventory(), true));
				} else {
					if (idPerson >= 0) {
						try {
							Restricted r = restrictedDAO.findByInventoryIdAndPersonId(iC.getInventory().getId(),
									idPerson);
							if (r != null) {
								result.add(dtoTools.toInventoryDTO(iC.getInventory(), true));
							}
						} catch (Exception e) {
							// TODO
						}
					}
				}
			}
			Collections.sort(result, new Comparator<InventoryDTO>() {
				@Override
				public int compare(final InventoryDTO object1, final InventoryDTO object2) {
					return object1.getOrder() - object2.getOrder();
				}
			});

		} catch (DAOException e) {
			e.printStackTrace();
		}

		return result;

	}

	@Override
	public ArrayList<ParametreDTO> findParametersFromCategoryIdAndEmail(Long categoryId, String email) {

		ArrayList<ParametreDTO> result = new ArrayList<ParametreDTO>();
		try {

			Category categoryFull = categoryDAO.findByID(categoryId, true);

			Set<Parametre> parametres = categoryFull.getParametres();
			for (Parametre parametre : parametres) {
				result.add(dtoTools.toParameterDTO(parametre, true));
			}
			Collections.sort(result, new OrderComparator());

		} catch (Exception e) {

		}
		return result;
	}

	@Override
	public String generateCsv(String csvFileName, Map<String, List<GridDTO>> gridData) {
		return "";

	}

	@Override
	public ArrayList<InventoryDTO> getInventoriesWithSectorsAndByCategoryId(Long categoryId) {

		ArrayList<InventoryDTO> result = new ArrayList<InventoryDTO>();

		try {

			List<Category> categories = categoryDAO.findByCategoryGroup(categoryId);
			List<InventoryCategory> invCats = new ArrayList<InventoryCategory>();

			for (Category category : categories) {

				List<InventoryCategory> tmp = inventoryCategoryDAO.findByCategoryId(category.getId());
				for (InventoryCategory inventoryCategory : tmp) {
					invCats.add(inventoryCategory);
				}
			}

			Map<String, Inventory> inventories = new HashMap<String, Inventory>();
			for (InventoryCategory invcat : invCats) {
				InventoryCategory inventoryCategory = inventoryCategoryDAO.findByID(invcat.getId(), true);
				inventories.put(inventoryCategory.getInventory().getTitleInventory(), inventoryCategory.getInventory());
			}

			for (String inventoryTitle : inventories.keySet()) {
				List<Sector> sectors = sectorDAO.findSectorByInventoryrId(inventories.get(inventoryTitle).getId());
				if (!sectors.isEmpty()) {
					result.add(dtoTools.toInventoryDTO(inventories.get(inventoryTitle), true));
				}
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		Collections.sort(result, new OrderComparator());

		return result;
	}

	@Override
	public String createMetadataPdf(InventoryDatasetDTO inventoryDatasetDTO) {
		return "";
	}

	@Override
	public List<ScenarioDTO> findScenarioByInventoryId(Long inventoryId) {

		List<ScenarioDTO> result = new ArrayList<ScenarioDTO>();
		try {
			List<Scenario> aux = scenarioDAO.findScenarioByInventoryrId(inventoryId);
			for (Scenario s : aux) {
				ScenarioDTO scenario = dtoTools.toScenarioDTO(s, true);
				result.add(scenario);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return result;

	}

	@Override
	public ArrayList<InventoryDTO> findInventoriesWithScenarios() {

		ArrayList<InventoryDTO> result = new ArrayList<InventoryDTO>();
		try {
			List<Inventory> aux = inventoryDAO.findInventoriesWithScenarios(true);
			for (Inventory inv : aux) {
				InventoryDTO inventory = dtoTools.toInventoryDTO(inv, true);
				result.add(inventory);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}

		Collections.sort(result, new OrderComparator());

		return result;

	}

	@Override
	public List<InventoryScenarioDatasetDTO> findInventoriesAndScenarios() {

		List<InventoryScenarioDatasetDTO> result = new ArrayList<InventoryScenarioDatasetDTO>();

		try {

			Set<Scenario> scenarios = scenarioDAO.findAll();

			for (Scenario scenario : scenarios) {
				InventoryScenarioDatasetDTO invScenarioDto = new InventoryScenarioDatasetDTO();
				invScenarioDto.setScenarioDto(dtoTools.toScenarioDTO(scenario, true));
				List<Inventory> inventories = inventoryDAO.findInventoriesByScenarioId(scenario.getId());
				ArrayList<InventoryDTO> invs = new ArrayList<InventoryDTO>();
				for (Inventory inventory : inventories) {
					invs.add(dtoTools.toInventoryDTO(inventory, true));
				}
				invScenarioDto.setInventories(invs);
				result.add(invScenarioDto);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	@Override
	public List<InventorySectorDatasetDTO> findInventoriesAndSectors() {
		List<InventorySectorDatasetDTO> result = new ArrayList<InventorySectorDatasetDTO>();

		try {

			Set<Sector> sectors = sectorDAO.findAll();

			for (Sector sector : sectors) {
				InventorySectorDatasetDTO invSectorDto = new InventorySectorDatasetDTO();
				invSectorDto.setSectorDto(dtoTools.toSectorDTO(sector, true));
				List<Inventory> inventories = inventoryDAO.findInventoriesBySectorId(sector.getId());

				Collections.sort(inventories, new Comparator<Inventory>() {
					@Override
					public int compare(Inventory inv1, Inventory inv2) {
						return inv1.getTitleInventory().compareTo(inv2.getTitleInventory());
					}
				});

				ArrayList<InventoryDTO> invs = new ArrayList<InventoryDTO>();
				for (Inventory inventory : inventories) {
					invs.add(dtoTools.toInventoryDTO(inventory, true));
				}
				invSectorDto.setInventories(invs);
				result.add(invSectorDto);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Collections.sort(result, new Comparator<InventorySectorDatasetDTO>() {

			@Override
			public int compare(InventorySectorDatasetDTO dataset1, InventorySectorDatasetDTO dataset2) {
				return dataset1.getSectorDto().getOrder() - dataset2.getSectorDto().getOrder();
			}

		});

		return result;
	}

	@Override
	public InventoryDTO findInventoryByName(String inventoryName) {
		InventoryDTO result = new InventoryDTO();
		try {
			Inventory inv = inventoryDAO.findByTitle(inventoryName, false);
			inv.setIdInventory(inv.getId());
			result = dtoTools.toInventoryDTO(inv, true);
		} catch (DAOException e) {
			e.printStackTrace();
		}
		return result;

	}

	@Override
	public ArrayList<PublicationDTO> findPublicationsByInventoryId(Long inventoryId) {

		ArrayList<PublicationDTO> publicationList = new ArrayList<PublicationDTO>();
		try {
			List<Publication> publications = publicationDAO.findByInventoryId(inventoryId);
			for (Publication pub : publications) {
				PublicationDTO pubDto = dtoTools.toPublicationDTO(pub);
				publicationList.add(pubDto);
			}

		} catch (DAOException e) {
			e.printStackTrace();
		}
		return publicationList;
	}

	// il faut convertir la liste d'organisation en liste d'organization DTO
	@Override
	public List<OrganizationDTO> findAllOrganizations() {
		// je crée une liste vide d'organisationDTO (DTO = Data TrAnsfert object)
		List<OrganizationDTO> organizationDTOsList = new ArrayList<OrganizationDTO>();

		try {
			// la methode findAll d'organizationDAO me renvoie un set d'organizations (un
			// ensemble)
			Set<Organization> organizations = organizationDAO.findAll();

			// pour chaque éléments de mon ensemble
			for (Organization org : organizations) {
				// je crée un nouvel organizationDTO depuis l'objet Organization
				OrganizationDTO orgDto = dtoTools.toOrganizationDTO(org);
				// j'ajoute l'organization DTO à la liste créée
				organizationDTOsList.add(orgDto);
			}
		} catch (DAOException e) {
			e.printStackTrace();
		}

		// je retourne la liste d'organizationDTO
		return organizationDTOsList;
	}

	// J'IMPLEMENTE LA METHODE (méthode findAllPersonsForOrganizations() qui permets
	// de trouver le nbre de pers. par organisation)
	@Override
	public int findAllPersonsForOrganizations(Long organizationId) {
		try {
			return personDAO.findNumberOfPersonsByOrganizationId(organizationId);
		} catch (DAOException e) {
			e.printStackTrace();
			return 0;
		}
	}

//-----------------------------------------------------------------------------------------------------------------------------------------------

	// il faut convertir la liste de person en liste de person DTO
	@Override
	public List<PersonDTO> findAllPersons() {
		// je crée une liste vide de personDTO (DTO = Data Transfert object)
		List<PersonDTO> personDTOsList = new ArrayList<PersonDTO>();

		try {
			// la methode findAll de personDAO me renvoie un set de personne (un ensemble)
			Set<Person> persons = personDAO.findAll();

			// pour chaque éléments de mon ensemble
			for (Person pers : persons) {
				// je crée une nouvelle personDTO depuis l'objet Person
				PersonDTO persDto = dtoTools.toPersonDTO(pers);
				// j'ajoute person DTO à la liste créée
				personDTOsList.add(persDto);
			}
		} catch (

		DAOException e) {
			e.printStackTrace();
		}

		// je retourne la liste de person DTO return personDTOsList;
		return personDTOsList;
	}

}
