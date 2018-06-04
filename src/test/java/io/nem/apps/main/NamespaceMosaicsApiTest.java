package io.nem.apps.main;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.junit.Ignore;
import org.junit.Test;
import org.nem.core.connect.client.NisApiId;
import org.nem.core.crypto.KeyPair;
import org.nem.core.crypto.PrivateKey;
import org.nem.core.model.Account;
import org.nem.core.model.MosaicDefinitionCreationTransaction;
import org.nem.core.model.ProvisionNamespaceTransaction;
import org.nem.core.model.TransferTransaction;
import org.nem.core.model.mosaic.DefaultMosaicProperties;
import org.nem.core.model.mosaic.Mosaic;
import org.nem.core.model.mosaic.MosaicDefinition;
import org.nem.core.model.mosaic.MosaicDescriptor;
import org.nem.core.model.mosaic.MosaicId;
import org.nem.core.model.mosaic.MosaicLevy;
import org.nem.core.model.mosaic.MosaicProperties;
import org.nem.core.model.mosaic.MosaicTransferFeeType;
import org.nem.core.model.namespace.NamespaceId;
import org.nem.core.model.namespace.NamespaceIdPart;
import org.nem.core.model.ncc.AccountMetaDataPair;
import org.nem.core.model.ncc.UnconfirmedTransactionMetaDataPair;
import org.nem.core.model.primitive.Quantity;
import org.nem.core.serialization.Deserializer;
import org.nem.core.test.Utils;
import org.nem.core.time.TimeInstant;

import io.nem.apps.api.AccountApi;
import io.nem.apps.api.NamespaceMosaicsApi;
import io.nem.apps.api.TransactionApi;
import io.nem.apps.service.NemAppsLibGlobals;
import io.nem.apps.util.TransactionSenderUtil;

public class NamespaceMosaicsApiTest extends NemAppsUnitTest {

	@Test
	public void testGetNamespaceRootPage() {
		try {
			
			assertNotNull(NamespaceMosaicsApi.getNamespaceRootPage());
		} catch (InterruptedException | ExecutionException e) {
			assert (false);
		}
		assert(true);
	}

	@Test
	public void testGetNamespace() {
		try {
			assertNotNull(NamespaceMosaicsApi.getNamespace("proximax"));
		} catch (InterruptedException | ExecutionException e) {
			assert (false);
		}
	}

	@Test
	public void testGetNamespaceMosaicDefinitionPage() {
		try {
			System.out.println(NamespaceMosaicsApi.getNamespaceMosaicDefinitionPage("proximax").get(0).getEntity().getId().getNamespaceId());
			assertNotNull(NamespaceMosaicsApi.getNamespaceMosaicDefinitionPage("proximax").get(0).getEntity().getId().getNamespaceId());
		} catch (InterruptedException | ExecutionException e) {
			assert (false);
		}
	}
	
	
	@Test
	public void testCreateNamespace() {
		Account SIGNER = new Account(new KeyPair(PrivateKey
				.fromHexString("deaae199f8e511ec51eb0046cf8d78dc481e20a340d003bbfcc3a66623d09763")));
		//final MosaicDefinition mosaicDefinition = Utils.createMosaicDefinition(SIGNER);
		TimeInstant ts = NemAppsLibGlobals.TIME_PROVIDER.getCurrentTime();
		ProvisionNamespaceTransaction provisionNamespaceTransaction = new ProvisionNamespaceTransaction(ts, SIGNER, new NamespaceIdPart("aa"), new NamespaceId("proximax"));
		
		provisionNamespaceTransaction.setDeadline(ts.addHours(24));
		provisionNamespaceTransaction.sign();
		TransactionSenderUtil.sendTransaction(provisionNamespaceTransaction);
	}
	
	@Test
	public void testCreateMosaicWithNamespace() {
		Account SIGNER = new Account(new KeyPair(PrivateKey
				.fromHexString("deaae199f8e511ec51eb0046cf8d78dc481e20a340d003bbfcc3a66623d09763")));
		//final MosaicDefinition mosaicDefinition = Utils.createMosaicDefinition(SIGNER);
		
		
		final MosaicDefinition mosaicDefinition = new MosaicDefinition(
				SIGNER, 
				new MosaicId(new NamespaceId("proximax"), "ava"), 
				new MosaicDescriptor("a12121a"),
				createMosaicProperties(0L, 3, null, null), 
				new MosaicLevy(MosaicTransferFeeType.Percentile, SIGNER, 
				new MosaicId(new NamespaceId("proximax"), "ava"),Quantity.fromValue(1l)));
		

		// Act:
		TimeInstant ts = NemAppsLibGlobals.TIME_PROVIDER.getCurrentTime();
		final MosaicDefinitionCreationTransaction transaction = new MosaicDefinitionCreationTransaction(ts, SIGNER, mosaicDefinition);
		transaction.setDeadline(ts.addHours(24));
		transaction.sign();
		TransactionSenderUtil.sendTransaction(transaction);
	}
	
	public static MosaicProperties createMosaicProperties(
			final Long initialSupply,
			final Integer divisibility,
			final Boolean isSupplyMutable,
			final Boolean isTransferable) {
		final Properties properties = new Properties();
		if (null != initialSupply) {
			properties.put("initialSupply", Long.toString(initialSupply));
		}

		if (null != divisibility) {
			properties.put("divisibility", Long.toString(divisibility));
		}

		if (null != isSupplyMutable) {
			properties.put("supplyMutable", Boolean.toString(isSupplyMutable));
		}

		if (null != isTransferable) {
			properties.put("transferable", Boolean.toString(isTransferable));
		}

		return new DefaultMosaicProperties(properties);
	}
	
	

}
