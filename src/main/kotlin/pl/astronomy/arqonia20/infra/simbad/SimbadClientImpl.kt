package pl.astronomy.arqonia20.infra.simbad

import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.client.WebClient
import pl.astronomy.arqonia20.domain.star.SimbadClient
import pl.astronomy.arqonia20.exceptions.SimbadClientException
import pl.astronomy.arqonia20.logger
import reactor.core.publisher.Mono

@Component
class SimbadClientImpl(
        simbadWebClient: WebClient,
        @Value("\${simbadClient.url}") private val url: String,
        @Value("\${simbadClient.query}") private val query: String
) : SimbadClient {
    private val client = simbadWebClient.mutate()
            .baseUrl(url)
            .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
            .build()

    override fun getAllIdentifiers(objectName: String): Mono<AllIdentifiers> =
            client.post()
                    .accept(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromFormData(formData(objectName)))
                    .retrieve()
                    .onStatus(HttpStatus::isError) {
                        Mono.error(SimbadClientException(it.statusCode()))
                    }
                    .bodyToMono(AllIdentifiers::class.java)

    private fun formData(objectName: String): MultiValueMap<String, String> {
        val formData: MultiValueMap<String, String> = LinkedMultiValueMap<String, String>()

        with(formData) {
            this.add("request", "doQuery")
            this.add("lang", "adql")
            this.add("format", "json")
            this.add("maxrec", "1000")
            this.add("runid", "Arqonia Sync Query")
            this.add("upload", "")
            this.add("query", String.format(query, objectName))
            this.add("phase", "run")
        }

        return formData
    }

    companion object {
        private val logger by logger()
    }

}
