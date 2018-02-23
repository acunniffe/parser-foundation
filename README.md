# Parser Foundation for Optic

This library is the foundation for all Optic Parsers. Once implemented, you'll be able to configure a parser to work with Optic's code generation and analysis tools.

### Prerequisites

* A target language that is parsed with a [context-free grammar](https://www.cs.rochester.edu/~nelson/courses/csc_173/grammars/cfg.html). This is the only hard requirement Optic has for program representations. 
* A parser for your target language that can run on the JVM or one of its supported scripted environments. If you're not sure where to start check out [ANTLR](https://github.com/antlr/antlr4/blob/master/README.md) which includes a JVM runtime and support for most popular programming languages. **Please Don't Build Your Own **
* Basic knowledge of [Abstract Syntax Trees](https://en.wikipedia.org/wiki/Abstract_syntax_tree)
* Scala / JVM Literacy

### Current / Upcoming Optic Parsers
* es6 by the Optic Team

## Getting Started

1. Create a new Scala project using sbt 1.0.* & scala 12.4.*
2. Include `parser-foundation` and its test classes from Maven Central 
```scala
libraryDependencies += "com.opticdev" %% "parser-foundation" % "0.1.0"
```
3. Create a package for your parser. Suggested format: `yourdomain.optic.parsers.{target}`
4. In that package create a new class that extends `ParserBase` named `OpticParser`. ** You must use the name OpticParser or Optic won't be able to find it when it dynamically loads your package. 
```scala
package com.mydomain.optic.parsers
import com.opticdev.parsers.ParserBase
class OpticParser extends ParserBase {
  /** The name of your language, must be alphanumeric  */
  override val languageName = _
  /** The semantic version of this parser package  */
  override val parserVersion = _
  /** A set of file extensions this parser should try to parse */
  override val fileExtensions = _
  /** The AST Type for root node for this language */
  override val programNodeType = _
  /** A list of the block nodes in this language with their AST Property Path to children
    * Example for Javascript
    * BlockNodeTypes(
    * BlockNodeDesc(AstType("BlockStatement", languageName), "body"),
    * BlockNodeDesc(AstType("Program", languageName), "body")
    * )
    * */
  override val blockNodeTypes = _
  /** The AST Type for the token node in this language and a path to its value
    * Example for Javascript
    * IdentifierNodeDesc(AstType("Identifier", languageName), Seq("name"))
    * */
  override val identifierNodeDesc = _
  /** Manually programmed interface for literals, tokens, object literals & arrays */
  override val basicSourceInterface = _
  /** Learned interface for all the rest of the nodes */
  override val marvinSourceInterface = _

  /** The AST Type for the token node in this language and a path to its value
    *
    * @param contents raw code to parse into a ParserResult object
    * */
  override def parseString(contents: String) = ???
}

```
### Code -> AST -> AST Graph
The most important part of your parser is the actual parser. Optic requires all ASTs to be transformed into a common format so it can be language agnostic.

Each AST Node will be represented by CommonAstNode which contains the AstType (node type), the range (start, end) of the node in its file, and its internal properties represented as a JsObject. 
```scala
case class CommonAstNode(nodeType: AstType, range: Range, properties: JsObject)
case class AstType(override val name: String, implicit val language: String) extends NodeType
```
In an AST representation the properties of a node contain both internal properties (name, value, final?) and references to other nodes (children[], body[], init, arguments[]). Since we are building a graph representation of the AST, your postprocessor must delineate between these types of properties. 
```json
{
  "type": "CallExpression", //becomes Node Type
  "start": 0, //becomes start in range
  "end": 34, //becomes end in range
  "exampleInternalProperty": false,  //becomes part of properties 
  "callee": {node1},  //becomes an edge of type Child(0, "callee", fromArray=false)
  "arguments": [{node1, node2, node3}] //becomes 3 edges of type Child(index, "callee", fromArray=true)
}
```  

#### Using the Graph Builder
We recommend using our `GraphBuilder` class in your postprocessor. You should create an instance of the graph builder once per parse operation.  
```scala
import com.opticdev.parsers.graph.GraphBuilder
val graphBuilder = new GraphBuilder[CommonAstNode]()
```
Call `rootPhase` to get a reference to the entry point for the file
```scala
val currentPhase = graphBuilder.rootPhase
```

To add children to the node call `addChild`. This returns an instance for the child you just inserted that you can also call `addChild` on.  
```scala
//example: 0, callee, CommonAstNode, false)
val childNodePhase = currentPhase.addChild(index, childType, node, fromArray)
```

When you're done call `graph` on the `GraphBuilder` instance to get a valid `AstGraph` you can return to Optic. 

Here's a good example from our es6 parser.  

## Building
Optic Parsers should be packaged into jars named `{targer}@{version}.jar`. If your parser relies on any external dependencies those must be included (in a fat jar) and will not be resolved at runtime by Optic. 

## Upcoming / Need Help With
* A generic postprocessor for the ANTLR runtime. 
* More prebuilt parsers for Optic (next up are Python & Swift)  
* More test fixtures 

## Publishing
If you want your parser to be listed on the Optic Registry please contact aidan@opticdev.com

## Contributing

Please read [CONTRIBUTING.md](CONTRIBUTING.md) for details on our code of conduct, and the process for submitting pull requests to us.

## License
This project is licensed under the MIT License
