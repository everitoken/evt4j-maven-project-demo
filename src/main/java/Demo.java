import java.util.Arrays;
import java.util.List;

import io.everitoken.sdk.java.Address;
import io.everitoken.sdk.java.Api;
import io.everitoken.sdk.java.Asset;
import io.everitoken.sdk.java.PrivateKey;
import io.everitoken.sdk.java.PublicKey;
import io.everitoken.sdk.java.abi.TransferFungibleAction;
import io.everitoken.sdk.java.dto.NodeInfo;
import io.everitoken.sdk.java.dto.TransactionData;
import io.everitoken.sdk.java.exceptions.ApiResponseException;
import io.everitoken.sdk.java.param.NetParams;
import io.everitoken.sdk.java.param.TestNetNetParams;
import io.everitoken.sdk.java.provider.KeyProvider;
import io.everitoken.sdk.java.service.TransactionConfiguration;
import io.everitoken.sdk.java.service.TransactionService;

public class Demo {
    public static void main(String[] args) {
        // generate a key pair
        PrivateKey privateKey = PrivateKey.randomPrivateKey();
        PublicKey publicKey = privateKey.toPublicKey();

        // construct a NetParams to interact with the node
        NetParams netParams = new TestNetNetParams();

        // init Api instance
        Api api = new Api(netParams);

        // get information of the node
        try {
            NodeInfo info = api.getInfo();
            System.out.println(info.getChainId());
        } catch (ApiResponseException ex) {
            System.out.println(ex.getRaw());
        }

        // get balance of all fungible tokens (for example: EVT Token) for a public key
        try {
            // do something with balance list
            List<Asset> balances = api.getFungibleBalance(Address.of(publicKey));

        } catch (ApiResponseException e) {
            System.out.println(e.getRaw());
        }

        // transfer fungible tokens to others

        // construct an action to represent the transfer
        TransferFungibleAction transferFungibleAction = TransferFungibleAction.of("2.00002 S#20",
                "EVT6Qz3wuRjyN6gaU3P3XRxpnEZnM4oPxortemaWDwFRvsv2FxgND",
                "EVT8aNw4NTvjBL1XR6hgy4zcA9jzh1JLjMuAw85mSbW68vYzw2f9H", "testing java");

        try {
            // init transaction service with net parameters
            TransactionService transactionService = TransactionService.of(netParams);

            NodeInfo nodeInfo = (new Api(netParams)).getInfo();
            KeyProvider keyProvider = KeyProvider.of(privateKey.toWif());
            // init transaction configuration
            TransactionConfiguration trxConfig =  TransactionConfiguration.of(nodeInfo, 1000000, publicKey );

            // push this action to the node and get back an transaction
            TransactionData txData = transactionService.push(trxConfig, Arrays.asList(transferFungibleAction), false,
                                                             keyProvider);
            System.out.println(txData.getTrxId());
        } catch (ApiResponseException ex) {
            System.out.println(ex.getRaw());
        }
    }
}
