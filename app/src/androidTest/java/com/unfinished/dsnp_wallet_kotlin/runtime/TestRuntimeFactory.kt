package com.unfinished.dsnp_wallet_kotlin.runtime

import android.content.Context
import com.google.gson.Gson
import com.unfinished.runtime.multiNetwork.runtime.types.custom.vote.SiVoteTypeMapping
import jp.co.soramitsu.fearless_utils.runtime.RuntimeSnapshot
import jp.co.soramitsu.fearless_utils.runtime.definitions.TypeDefinitionParser.parseBaseDefinitions
import jp.co.soramitsu.fearless_utils.runtime.definitions.TypeDefinitionParser.parseNetworkVersioning
import jp.co.soramitsu.fearless_utils.runtime.definitions.TypeDefinitionsTree
import jp.co.soramitsu.fearless_utils.runtime.definitions.dynamic.DynamicTypeResolver
import jp.co.soramitsu.fearless_utils.runtime.definitions.dynamic.extentsions.GenericsExtension
import jp.co.soramitsu.fearless_utils.runtime.definitions.registry.TypePreset
import jp.co.soramitsu.fearless_utils.runtime.definitions.registry.TypeRegistry
import jp.co.soramitsu.fearless_utils.runtime.definitions.registry.v13Preset
import jp.co.soramitsu.fearless_utils.runtime.definitions.registry.v14Preset
import jp.co.soramitsu.fearless_utils.runtime.definitions.v14.TypesParserV14
import jp.co.soramitsu.fearless_utils.runtime.definitions.v14.typeMapping.SiTypeMapping
import jp.co.soramitsu.fearless_utils.runtime.definitions.v14.typeMapping.default
import jp.co.soramitsu.fearless_utils.runtime.definitions.v14.typeMapping.plus
import jp.co.soramitsu.fearless_utils.runtime.metadata.RuntimeMetadataReader
import jp.co.soramitsu.fearless_utils.runtime.metadata.builder.VersionedRuntimeBuilder
import jp.co.soramitsu.fearless_utils.runtime.metadata.v14.RuntimeMetadataSchemaV14
import java.io.IOException

class TestRuntimeFactory {

    val testRuntimeVersion = 1
    val METADATA_FILE_MASK = "metadata_%s"

    suspend fun constructRuntimeInternal(runtimeMetadataRaw: String, types: String): RuntimeSnapshot {
        val metadataReader = RuntimeMetadataReader.read(runtimeMetadataRaw)

        val typePreset = if (metadataReader.metadataVersion < 14) {
            v13Preset()
        } else {
            TypesParserV14.parse(
                lookup = metadataReader.metadata[RuntimeMetadataSchemaV14.lookup],
                typePreset = v14Preset(),
                typeMapping = allSiTypeMappings()
            )
        }
        val (types, ownHash) = constructOwnTypes(testRuntimeVersion, typePreset,types)
        Triple(types, null, ownHash)
        val typeRegistry = TypeRegistry(types, DynamicTypeResolver(DynamicTypeResolver.DEFAULT_COMPOUND_EXTENSIONS + GenericsExtension))
        val runtimeMetadata = VersionedRuntimeBuilder.buildMetadata(metadataReader, typeRegistry)
        return RuntimeSnapshot(typeRegistry, runtimeMetadata)
    }

    private fun constructOwnTypes(
        runtimeVersion: Int,
        baseTypes: TypePreset,
        ownTypesRaw: String,
    ): Pair<TypePreset, String> {
        val ownTypesTree = fromJson(ownTypesRaw)

        val withoutVersioning = parseBaseDefinitions(ownTypesTree, baseTypes)

        val typePreset = parseNetworkVersioning(ownTypesTree, withoutVersioning, runtimeVersion)

        return typePreset to ownTypesRaw.md5()
    }

    private fun fromJson(types: String): TypeDefinitionsTree = Gson().fromJson(types, TypeDefinitionsTree::class.java)

    private fun allSiTypeMappings() = SiTypeMapping.default() + SiVoteTypeMapping

    fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }
}
