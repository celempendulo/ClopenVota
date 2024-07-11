package io.clopen.vota.db;

import com.arangodb.entity.CollectionType;
import com.arangodb.model.CollectionCreateOptions;
import com.arangodb.springframework.annotation.Document;
import com.arangodb.springframework.annotation.Edge;
import com.arangodb.springframework.core.ArangoOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

@SuppressWarnings("unused")
@Component
public class Initializer {

  private final ArangoOperations operations;

  @Autowired
  public Initializer(ArangoOperations operations) {
    this.operations = operations;
  }

  public void init() {
    createDocuments();
    createEdges();
  }

  private void createEdges() {
    var scanner = new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(new AnnotationTypeFilter(Edge.class));
    try {
      for (var component : scanner.findCandidateComponents("io.clopen")) {
        var doc = Class.forName(component.getBeanClassName()).getAnnotation(Edge.class);
        operations.collection(doc.value(), new CollectionCreateOptions().type(CollectionType.EDGES));
      }
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }

  private void createDocuments() {
    var scanner = new ClassPathScanningCandidateComponentProvider(false);
    scanner.addIncludeFilter(new AnnotationTypeFilter(Document.class));
    try {
      for (var component : scanner.findCandidateComponents("io.clopen")) {
        var doc = Class.forName(component.getBeanClassName()).getAnnotation(Document.class);
        operations.collection(doc.value());
      }
    } catch (ClassNotFoundException e) {
      throw new RuntimeException(e);
    }
  }
}

