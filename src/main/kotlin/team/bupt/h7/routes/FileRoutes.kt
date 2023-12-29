package team.bupt.h7.routes

import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.core.client.config.ClientOverrideConfiguration
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.File
import java.net.URI
import java.time.Duration

fun Route.fileRouting() {
    route("/files") {
        post("/upload") {
            val multipart = call.receiveMultipart()
            var url = ""
            multipart.forEachPart { part ->
                when (part) {
                    is PartData.FileItem -> {
                        val file = File("uploads/" + part.originalFileName)
                        part.streamProvider().use { its ->
                            file.outputStream().buffered().use {
                                its.copyTo(it)
                            }
                        }

                        url = uploadToCloudflareR2(file)
                    }

                    is PartData.FormItem -> {
                        println("FormItem: ${part.name} = ${part.value}")
                    }

                    else -> {
                        println("Unknown part type: $part")
                    }
                }
                part.dispose()
            }
            call.respondText(url)
        }
    }
}

fun uploadToCloudflareR2(file: File): String {
    val accessKey = System.getenv("CLOUDFLARE_ACCESS_KEY")!!
    val secretKey = System.getenv("CLOUDFLARE_SECRET_KEY")!!
    val accountId = System.getenv("CLOUDFLARE_ACCOUNT_ID")!!
    val bucketName = System.getenv("CLOUDFLARE_BUCKET_NAME")!!
    val publicUrl = System.getenv("CLOUDFLARE_PUBLIC_URL")!!

    val awsCreds = AwsBasicCredentials.create(accessKey, secretKey)

    val s3 = S3Client.builder()
        .region(Region.of("auto")) // Cloudflare R2 does not require a region
        .credentialsProvider { awsCreds }
        .endpointOverride(URI.create("https://$accountId.r2.cloudflarestorage.com"))
        .overrideConfiguration(
            ClientOverrideConfiguration.builder()
                .apiCallTimeout(Duration.ofSeconds(30))
                .build()
        )
        .build()

    s3.putObject(
        PutObjectRequest.builder()
            .bucket(bucketName)
            .key(file.name)
            .build(),
        file.toPath()
    )

    return "$publicUrl/${file.name}"
}