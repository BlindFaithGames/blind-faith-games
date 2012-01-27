// Automatically Generated -- DO NOT EDIT
// com.minesweeper.client.MyRequestFactory
package com.minesweeper.client;
import java.util.Arrays;
import com.google.web.bindery.requestfactory.vm.impl.OperationData;
import com.google.web.bindery.requestfactory.vm.impl.OperationKey;
public final class MyRequestFactoryDeobfuscatorBuilder extends com.google.web.bindery.requestfactory.vm.impl.Deobfuscator.Builder {
{
withOperation(new OperationKey("L_bCMh7c$0kkLMaUAxdA1pjm6aI="),
  new OperationData.Builder()
  .withClientMethodDescriptor("(Ljava/lang/Long;)Lcom/google/web/bindery/requestfactory/shared/Request;")
  .withDomainMethodDescriptor("(Ljava/lang/Long;)Lcom/minesweeper/server/Log;")
  .withMethodName("readLog")
  .withRequestContext("com.minesweeper.client.MyRequestFactory$LogRequest")
  .build());
withOperation(new OperationKey("ZbAkCd46Nr2PQwjU_Zav7LI9Wrg="),
  new OperationData.Builder()
  .withClientMethodDescriptor("(Ljava/lang/String;)Lcom/google/web/bindery/requestfactory/shared/Request;")
  .withDomainMethodDescriptor("(Ljava/lang/String;)Ljava/util/List;")
  .withMethodName("readLogs")
  .withRequestContext("com.minesweeper.client.MyRequestFactory$LogRequest")
  .build());
withOperation(new OperationKey("8wBJA63daPCuU1ofLpd85q9Xkdo="),
  new OperationData.Builder()
  .withClientMethodDescriptor("(Lcom/minesweeper/shared/LogProxy;)Lcom/google/web/bindery/requestfactory/shared/Request;")
  .withDomainMethodDescriptor("(Lcom/minesweeper/server/Log;)Lcom/minesweeper/server/Log;")
  .withMethodName("createLog")
  .withRequestContext("com.minesweeper.client.MyRequestFactory$LogRequest")
  .build());
withOperation(new OperationKey("_ej2zoLAU7x4SGTZNdojBe$M_PI="),
  new OperationData.Builder()
  .withClientMethodDescriptor("(Lcom/minesweeper/shared/LogProxy;)Lcom/google/web/bindery/requestfactory/shared/Request;")
  .withDomainMethodDescriptor("(Lcom/minesweeper/server/Log;)V")
  .withMethodName("deleteLog")
  .withRequestContext("com.minesweeper.client.MyRequestFactory$LogRequest")
  .build());
withOperation(new OperationKey("8kaHCW8o9qwEbI9tpFz5O1D$cMs="),
  new OperationData.Builder()
  .withClientMethodDescriptor("(Lcom/minesweeper/shared/LogProxy;)Lcom/google/web/bindery/requestfactory/shared/Request;")
  .withDomainMethodDescriptor("(Lcom/minesweeper/server/Log;)Lcom/minesweeper/server/Log;")
  .withMethodName("updateLog")
  .withRequestContext("com.minesweeper.client.MyRequestFactory$LogRequest")
  .build());
withOperation(new OperationKey("XDrp2WHuAQs3sRfNVxZq_MgjEyw="),
  new OperationData.Builder()
  .withClientMethodDescriptor("()Lcom/google/web/bindery/requestfactory/shared/Request;")
  .withDomainMethodDescriptor("()Ljava/util/List;")
  .withMethodName("queryLogs")
  .withRequestContext("com.minesweeper.client.MyRequestFactory$LogRequest")
  .build());
withOperation(new OperationKey("i_5HHlARpeYRmlVUwh8p9PSLpBM="),
  new OperationData.Builder()
  .withClientMethodDescriptor("()Lcom/google/web/bindery/requestfactory/shared/InstanceRequest;")
  .withDomainMethodDescriptor("()Ljava/lang/String;")
  .withMethodName("send")
  .withRequestContext("com.minesweeper.client.MyRequestFactory$MessageRequest")
  .build());
withOperation(new OperationKey("0FvjTqBuz3JsjW40cWXYMud77GE="),
  new OperationData.Builder()
  .withClientMethodDescriptor("()Lcom/google/web/bindery/requestfactory/shared/InstanceRequest;")
  .withDomainMethodDescriptor("()V")
  .withMethodName("register")
  .withRequestContext("com.minesweeper.client.MyRequestFactory$RegistrationInfoRequest")
  .build());
withOperation(new OperationKey("XNqYf1h7R$z$XeFFVIu7fTwr1ik="),
  new OperationData.Builder()
  .withClientMethodDescriptor("()Lcom/google/web/bindery/requestfactory/shared/InstanceRequest;")
  .withDomainMethodDescriptor("()V")
  .withMethodName("unregister")
  .withRequestContext("com.minesweeper.client.MyRequestFactory$RegistrationInfoRequest")
  .build());
withOperation(new OperationKey("8a6oY0XH3PJf3M6S75FPAatn4kQ="),
  new OperationData.Builder()
  .withClientMethodDescriptor("(Lcom/minesweeper/shared/EntryProxy;)Lcom/google/web/bindery/requestfactory/shared/Request;")
  .withDomainMethodDescriptor("(Lcom/minesweeper/server/Entry;)Ljava/lang/Long;")
  .withMethodName("createEntry")
  .withRequestContext("com.minesweeper.client.MyRequestFactory$EntryRequest")
  .build());
withOperation(new OperationKey("2FkP0CY$ArWl1x8RVUAFQdZLsoA="),
  new OperationData.Builder()
  .withClientMethodDescriptor("(Ljava/util/List;Ljava/lang/String;)Lcom/google/web/bindery/requestfactory/shared/Request;")
  .withDomainMethodDescriptor("(Ljava/util/List;Ljava/lang/String;)Ljava/util/List;")
  .withMethodName("readEntriesByEvent")
  .withRequestContext("com.minesweeper.client.MyRequestFactory$EntryRequest")
  .build());
withOperation(new OperationKey("KLHpr_bk5Uck5YmG4nFGmfx3h54="),
  new OperationData.Builder()
  .withClientMethodDescriptor("(Ljava/lang/Long;)Lcom/google/web/bindery/requestfactory/shared/Request;")
  .withDomainMethodDescriptor("(Ljava/lang/Long;)Lcom/minesweeper/server/Entry;")
  .withMethodName("readEntry")
  .withRequestContext("com.minesweeper.client.MyRequestFactory$EntryRequest")
  .build());
withOperation(new OperationKey("6iE2mq8Uj7QmVOgcTK3SEDFJiVk="),
  new OperationData.Builder()
  .withClientMethodDescriptor("(Lcom/minesweeper/shared/EntryProxy;)Lcom/google/web/bindery/requestfactory/shared/Request;")
  .withDomainMethodDescriptor("(Lcom/minesweeper/server/Entry;)V")
  .withMethodName("deleteEntry")
  .withRequestContext("com.minesweeper.client.MyRequestFactory$EntryRequest")
  .build());
withOperation(new OperationKey("XAwxbjbDT6qs23Bi_SgQ4vBH7oQ="),
  new OperationData.Builder()
  .withClientMethodDescriptor("(Lcom/minesweeper/shared/EntryProxy;)Lcom/google/web/bindery/requestfactory/shared/Request;")
  .withDomainMethodDescriptor("(Lcom/minesweeper/server/Entry;)Ljava/lang/Long;")
  .withMethodName("updateEntry")
  .withRequestContext("com.minesweeper.client.MyRequestFactory$EntryRequest")
  .build());
withOperation(new OperationKey("IxUtLU6_LH3v5nDpw_qbHdIU7ng="),
  new OperationData.Builder()
  .withClientMethodDescriptor("()Lcom/google/web/bindery/requestfactory/shared/Request;")
  .withDomainMethodDescriptor("()Ljava/util/List;")
  .withMethodName("queryEntries")
  .withRequestContext("com.minesweeper.client.MyRequestFactory$EntryRequest")
  .build());
withOperation(new OperationKey("LB_bvhadapsoBzrB2s8_$XrY7IE="),
  new OperationData.Builder()
  .withClientMethodDescriptor("(Ljava/util/List;)Lcom/google/web/bindery/requestfactory/shared/Request;")
  .withDomainMethodDescriptor("(Ljava/util/List;)Ljava/util/List;")
  .withMethodName("readEntries")
  .withRequestContext("com.minesweeper.client.MyRequestFactory$EntryRequest")
  .build());
withRawTypeToken("8KVVbwaaAtl6KgQNlOTsLCp9TIU=", "com.google.web.bindery.requestfactory.shared.ValueProxy");
withRawTypeToken("FXHD5YU0TiUl3uBaepdkYaowx9k=", "com.google.web.bindery.requestfactory.shared.BaseProxy");
withRawTypeToken("OKK6TnWAUWJErgVCm_nMoFeGP5k=", "com.minesweeper.shared.EntryProxy");
withRawTypeToken("wEnC9tVMJEsGbIkGxCEAKs9_fUw=", "com.minesweeper.shared.LogProxy");
withRawTypeToken("Gz6J7SAb$Wf6n_8oohvqrHc4y4k=", "com.minesweeper.shared.MessageProxy");
withRawTypeToken("tTgBxhplRTONaa105Pui9_$AEmw=", "com.minesweeper.shared.RegistrationInfoProxy");
withClientToDomainMappings("com.minesweeper.server.Entry", Arrays.asList("com.minesweeper.shared.EntryProxy"));
withClientToDomainMappings("com.minesweeper.server.Log", Arrays.asList("com.minesweeper.shared.LogProxy"));
withClientToDomainMappings("com.minesweeper.server.Message", Arrays.asList("com.minesweeper.shared.MessageProxy"));
withClientToDomainMappings("com.minesweeper.server.RegistrationInfo", Arrays.asList("com.minesweeper.shared.RegistrationInfoProxy"));
}}
