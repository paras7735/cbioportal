package org.cbioportal.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.cbioportal.model.DiscreteCopyNumberData;
import org.cbioportal.model.Gene;
import org.cbioportal.model.meta.BaseMeta;
import org.cbioportal.service.DiscreteCopyNumberService;
import org.cbioportal.web.parameter.DiscreteCopyNumberEventType;
import org.cbioportal.web.parameter.DiscreteCopyNumberFilter;
import org.cbioportal.web.parameter.HeaderKeyConstants;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration("/applicationContext-web.xml")
@Configuration
public class DiscreteCopyNumberControllerTest {
    
    private static final String TEST_GENETIC_PROFILE_STABLE_ID_1 = "test_genetic_profile_stable_id_1";
    private static final String TEST_SAMPLE_STABLE_ID_1 = "test_sample_stable_id_1";
    private static final int TEST_ENTREZ_GENE_ID_1 = 1;
    private static final int TEST_ALTERATION_1 = 1;
    private static final String TEST_HUGO_GENE_SYMBOL_1 = "test_hugo_gene_symbol_1";
    private static final String TEST_TYPE_1 = "test_type_1";
    private static final String TEST_CYTOBAND_1 = "test_cytoband_1";
    private static final int TEST_LENGTH_1 = 100;
    private static final String TEST_CHROMOSOME_1 = "test_chromosome_1";
    private static final String TEST_GENETIC_PROFILE_STABLE_ID_2 = "test_genetic_profile_stable_id_2";
    private static final String TEST_SAMPLE_STABLE_ID_2 = "test_sample_stable_id_2";
    private static final int TEST_ENTREZ_GENE_ID_2 = 2;
    private static final int TEST_ALTERATION_2 = 2;
    private static final String TEST_HUGO_GENE_SYMBOL_2 = "test_hugo_gene_symbol_2";
    private static final String TEST_TYPE_2 = "test_type_2";
    private static final String TEST_CYTOBAND_2 = "test_cytoband_2";
    private static final int TEST_LENGTH_2 = 200;
    private static final String TEST_CHROMOSOME_2 = "test_chromosome_2";
    private static final String TEST_SAMPLE_LIST_ID = "test_sample_list_id";

    @Autowired
    private WebApplicationContext wac;

    @Autowired
    private DiscreteCopyNumberService discreteCopyNumberService;
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Bean
    public DiscreteCopyNumberService discreteCopyNumberService() {
        return Mockito.mock(DiscreteCopyNumberService.class);
    }

    @Before
    public void setUp() throws Exception {

        Mockito.reset(discreteCopyNumberService);
        mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
    }
    
    @Test
    public void getDiscreteCopyNumbersInGeneticProfileBySampleListIdDefaultProjection() throws Exception {

        List<DiscreteCopyNumberData> discreteCopyNumberDataList = createExampleDiscreteCopyNumberData();

        Mockito.when(discreteCopyNumberService.getDiscreteCopyNumbersInGeneticProfileBySampleListId(Mockito.anyString(), 
            Mockito.anyString(), Mockito.anyListOf(Integer.class), Mockito.anyString()))
            .thenReturn(discreteCopyNumberDataList);

        mockMvc.perform(MockMvcRequestBuilders.get("/genetic-profiles/test_genetic_profile_id/discrete-copy-number")
            .param("sampleListId", TEST_SAMPLE_LIST_ID)
            .param("discreteCopyNumberEventType", DiscreteCopyNumberEventType.HOMDEL_AND_AMP.name())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].geneticProfileId").value(TEST_GENETIC_PROFILE_STABLE_ID_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].sampleId").value(TEST_SAMPLE_STABLE_ID_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].entrezGeneId").value(TEST_ENTREZ_GENE_ID_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].alteration").value(TEST_ALTERATION_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].gene").doesNotExist())
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].geneticProfileId").value(TEST_GENETIC_PROFILE_STABLE_ID_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].sampleId").value(TEST_SAMPLE_STABLE_ID_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].entrezGeneId").value(TEST_ENTREZ_GENE_ID_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].alteration").value(TEST_ALTERATION_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].gene").doesNotExist());
    }

    @Test
    public void getDiscreteCopyNumbersInGeneticProfileBySampleListIdDetailedProjection() throws Exception {

        List<DiscreteCopyNumberData> discreteCopyNumberDataList = createExampleDiscreteCopyNumberDataWithGenes();

        Mockito.when(discreteCopyNumberService.getDiscreteCopyNumbersInGeneticProfileBySampleListId(Mockito.anyString(),
            Mockito.anyString(), Mockito.anyListOf(Integer.class), Mockito.anyString()))
            .thenReturn(discreteCopyNumberDataList);

        mockMvc.perform(MockMvcRequestBuilders.get("/genetic-profiles/test_genetic_profile_id/discrete-copy-number")
            .param("sampleListId", TEST_SAMPLE_LIST_ID)
            .param("discreteCopyNumberEventType", DiscreteCopyNumberEventType.HOMDEL_AND_AMP.name())
            .param("projection", "DETAILED")
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].geneticProfileId").value(TEST_GENETIC_PROFILE_STABLE_ID_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].sampleId").value(TEST_SAMPLE_STABLE_ID_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].entrezGeneId").value(TEST_ENTREZ_GENE_ID_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].alteration").value(TEST_ALTERATION_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].gene.entrezGeneId").value(TEST_ENTREZ_GENE_ID_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].gene.hugoGeneSymbol").value(TEST_HUGO_GENE_SYMBOL_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].gene.type").value(TEST_TYPE_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].gene.cytoband").value(TEST_CYTOBAND_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].gene.length").value(TEST_LENGTH_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].gene.chromosome").value(TEST_CHROMOSOME_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].geneticProfileId").value(TEST_GENETIC_PROFILE_STABLE_ID_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].sampleId").value(TEST_SAMPLE_STABLE_ID_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].entrezGeneId").value(TEST_ENTREZ_GENE_ID_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].alteration").value(TEST_ALTERATION_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].gene.entrezGeneId").value(TEST_ENTREZ_GENE_ID_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].gene.hugoGeneSymbol").value(TEST_HUGO_GENE_SYMBOL_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].gene.type").value(TEST_TYPE_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].gene.cytoband").value(TEST_CYTOBAND_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].gene.length").value(TEST_LENGTH_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].gene.chromosome").value(TEST_CHROMOSOME_2));
    }

    @Test
    public void getDiscreteCopyNumbersInGeneticProfileBySampleListIdMetaProjection() throws Exception {

        BaseMeta baseMeta = new BaseMeta();
        baseMeta.setTotalCount(2);

        Mockito.when(discreteCopyNumberService.getMetaDiscreteCopyNumbersInGeneticProfileBySampleListId(
            Mockito.anyString(), Mockito.anyString(), Mockito.anyListOf(Integer.class))).thenReturn(baseMeta);

        mockMvc.perform(MockMvcRequestBuilders.get("/genetic-profiles/test_genetic_profile_id/discrete-copy-number")
            .param("sampleListId", TEST_SAMPLE_LIST_ID)
            .param("discreteCopyNumberEventType", DiscreteCopyNumberEventType.HOMDEL_AND_AMP.name())
            .param("projection", "META"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.header().string(HeaderKeyConstants.TOTAL_COUNT, "2"));
    }

    @Test
    public void fetchDiscreteCopyNumbersInGeneticProfileDefaultProjection() throws Exception {

        List<DiscreteCopyNumberData> discreteCopyNumberDataList = createExampleDiscreteCopyNumberData();

        Mockito.when(discreteCopyNumberService.fetchDiscreteCopyNumbersInGeneticProfile(Mockito.anyString(),
            Mockito.anyListOf(String.class), Mockito.anyListOf(Integer.class), Mockito.anyListOf(Integer.class),
            Mockito.anyString())).thenReturn(discreteCopyNumberDataList);

        DiscreteCopyNumberFilter discreteCopyNumberFilter = createDiscreteCopyNumberFilter();

        mockMvc.perform(MockMvcRequestBuilders
            .post("/genetic-profiles/test_genetic_profile_id/discrete-copy-number/fetch")
            .param("discreteCopyNumberEventType", DiscreteCopyNumberEventType.HOMDEL_AND_AMP.name())
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(discreteCopyNumberFilter)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].geneticProfileId").value(TEST_GENETIC_PROFILE_STABLE_ID_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].sampleId").value(TEST_SAMPLE_STABLE_ID_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].entrezGeneId").value(TEST_ENTREZ_GENE_ID_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].alteration").value(TEST_ALTERATION_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].gene").doesNotExist())
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].geneticProfileId").value(TEST_GENETIC_PROFILE_STABLE_ID_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].sampleId").value(TEST_SAMPLE_STABLE_ID_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].entrezGeneId").value(TEST_ENTREZ_GENE_ID_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].alteration").value(TEST_ALTERATION_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].gene").doesNotExist());
    }

    @Test
    public void fetchDiscreteCopyNumbersInGeneticProfileDetailedProjection() throws Exception {

        List<DiscreteCopyNumberData> discreteCopyNumberDataList = createExampleDiscreteCopyNumberDataWithGenes();

        Mockito.when(discreteCopyNumberService.fetchDiscreteCopyNumbersInGeneticProfile(Mockito.anyString(),
            Mockito.anyListOf(String.class), Mockito.anyListOf(Integer.class), Mockito.anyListOf(Integer.class),
            Mockito.anyString()))
            .thenReturn(discreteCopyNumberDataList);

        DiscreteCopyNumberFilter discreteCopyNumberFilter = createDiscreteCopyNumberFilter();
        
        mockMvc.perform(MockMvcRequestBuilders
            .post("/genetic-profiles/test_genetic_profile_id/discrete-copy-number/fetch")
            .param("discreteCopyNumberEventType", DiscreteCopyNumberEventType.HOMDEL_AND_AMP.name())
            .param("projection", "DETAILED")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(discreteCopyNumberFilter)))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].geneticProfileId").value(TEST_GENETIC_PROFILE_STABLE_ID_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].sampleId").value(TEST_SAMPLE_STABLE_ID_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].entrezGeneId").value(TEST_ENTREZ_GENE_ID_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].alteration").value(TEST_ALTERATION_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].gene.entrezGeneId").value(TEST_ENTREZ_GENE_ID_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].gene.hugoGeneSymbol").value(TEST_HUGO_GENE_SYMBOL_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].gene.type").value(TEST_TYPE_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].gene.cytoband").value(TEST_CYTOBAND_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].gene.length").value(TEST_LENGTH_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[0].gene.chromosome").value(TEST_CHROMOSOME_1))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].geneticProfileId").value(TEST_GENETIC_PROFILE_STABLE_ID_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].sampleId").value(TEST_SAMPLE_STABLE_ID_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].entrezGeneId").value(TEST_ENTREZ_GENE_ID_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].alteration").value(TEST_ALTERATION_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].gene.entrezGeneId").value(TEST_ENTREZ_GENE_ID_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].gene.hugoGeneSymbol").value(TEST_HUGO_GENE_SYMBOL_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].gene.type").value(TEST_TYPE_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].gene.cytoband").value(TEST_CYTOBAND_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].gene.length").value(TEST_LENGTH_2))
            .andExpect(MockMvcResultMatchers.jsonPath("$[1].gene.chromosome").value(TEST_CHROMOSOME_2));
    }

    @Test
    public void fetchDiscreteCopyNumbersInGeneticProfileMetaProjection() throws Exception {

        BaseMeta baseMeta = new BaseMeta();
        baseMeta.setTotalCount(2);

        Mockito.when(discreteCopyNumberService.fetchMetaDiscreteCopyNumbersInGeneticProfile(Mockito.anyString(),
            Mockito.anyListOf(String.class), Mockito.anyListOf(Integer.class), Mockito.anyListOf(Integer.class)))
            .thenReturn(baseMeta);

        DiscreteCopyNumberFilter discreteCopyNumberFilter = createDiscreteCopyNumberFilter();
        
        mockMvc.perform(MockMvcRequestBuilders.
            post("/genetic-profiles/test_genetic_profile_id/discrete-copy-number/fetch")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(discreteCopyNumberFilter))
            .param("discreteCopyNumberEventType", DiscreteCopyNumberEventType.HOMDEL_AND_AMP.name())
            .param("projection", "META"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.header().string(HeaderKeyConstants.TOTAL_COUNT, "2"));
    }

    private DiscreteCopyNumberFilter createDiscreteCopyNumberFilter() {
        List<String> sampleIds = new ArrayList<>();
        sampleIds.add(TEST_SAMPLE_STABLE_ID_1);
        sampleIds.add(TEST_SAMPLE_STABLE_ID_2);

        List<Integer> entrezGeneIds = new ArrayList<>();
        entrezGeneIds.add(TEST_ENTREZ_GENE_ID_1);
        entrezGeneIds.add(TEST_ENTREZ_GENE_ID_2);

        DiscreteCopyNumberFilter discreteCopyNumberFilter = new DiscreteCopyNumberFilter();
        discreteCopyNumberFilter.setEntrezGeneIds(entrezGeneIds);
        discreteCopyNumberFilter.setSampleIds(sampleIds);
        return discreteCopyNumberFilter;
    }

    private List<DiscreteCopyNumberData> createExampleDiscreteCopyNumberData() {
        
        List<DiscreteCopyNumberData> discreteCopyNumberDataList = new ArrayList<>();
        DiscreteCopyNumberData discreteCopyNumberData1 = new DiscreteCopyNumberData();
        discreteCopyNumberData1.setGeneticProfileId(TEST_GENETIC_PROFILE_STABLE_ID_1);
        discreteCopyNumberData1.setSampleId(TEST_SAMPLE_STABLE_ID_1);
        discreteCopyNumberData1.setEntrezGeneId(TEST_ENTREZ_GENE_ID_1);
        discreteCopyNumberData1.setAlteration(TEST_ALTERATION_1);
        discreteCopyNumberDataList.add(discreteCopyNumberData1);
        DiscreteCopyNumberData discreteCopyNumberData2 = new DiscreteCopyNumberData();
        discreteCopyNumberData2.setGeneticProfileId(TEST_GENETIC_PROFILE_STABLE_ID_2);
        discreteCopyNumberData2.setSampleId(TEST_SAMPLE_STABLE_ID_2);
        discreteCopyNumberData2.setEntrezGeneId(TEST_ENTREZ_GENE_ID_2);
        discreteCopyNumberData2.setAlteration(TEST_ALTERATION_2);
        discreteCopyNumberDataList.add(discreteCopyNumberData2);
        return discreteCopyNumberDataList;        
    }

    private List<DiscreteCopyNumberData> createExampleDiscreteCopyNumberDataWithGenes() {

        List<DiscreteCopyNumberData> discreteCopyNumberDataList = createExampleDiscreteCopyNumberData();
        Gene gene1 = new Gene();
        gene1.setEntrezGeneId(TEST_ENTREZ_GENE_ID_1);
        gene1.setHugoGeneSymbol(TEST_HUGO_GENE_SYMBOL_1);
        gene1.setType(TEST_TYPE_1);
        gene1.setCytoband(TEST_CYTOBAND_1);
        gene1.setLength(TEST_LENGTH_1);
        gene1.setChromosome(TEST_CHROMOSOME_1);
        discreteCopyNumberDataList.get(0).setGene(gene1);
        Gene gene2 = new Gene();
        gene2.setEntrezGeneId(TEST_ENTREZ_GENE_ID_2);
        gene2.setHugoGeneSymbol(TEST_HUGO_GENE_SYMBOL_2);
        gene2.setType(TEST_TYPE_2);
        gene2.setCytoband(TEST_CYTOBAND_2);
        gene2.setLength(TEST_LENGTH_2);
        gene2.setChromosome(TEST_CHROMOSOME_2);
        discreteCopyNumberDataList.get(1).setGene(gene2);
        return discreteCopyNumberDataList;
    }
}